/*
 * Copyright (C) 2005-2010 ManyDesigns srl.  All rights reserved.
 * http://www.manydesigns.com/
 *
 * Unless you have purchased a commercial license agreement from ManyDesigns srl,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * There are special exceptions to the terms and conditions of the GPL
 * as it is applied to this software. View the full text of the
 * exception in file OPEN-SOURCE-LICENSE.txt in the directory of this
 * software distribution.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307  USA
 *
 */

package com.manydesigns.portofino.context.hibernate;

import com.manydesigns.elements.logging.LogUtil;
import com.manydesigns.portofino.context.MDContext;
import com.manydesigns.portofino.database.ConnectionProvider;
import com.manydesigns.portofino.database.DatabaseAbstraction;
import com.manydesigns.portofino.database.DatabaseAbstractionManager;
import com.manydesigns.portofino.database.JdbcConnectionProvider;
import com.manydesigns.portofino.model.*;
import com.manydesigns.portofino.model.io.DBParser;
import com.manydesigns.portofino.search.HibernateCriteriaAdapter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
*/
public class MDContextHibernateImpl implements MDContext {
    public static final String copyright =
            "Copyright (c) 2005-2010, ManyDesigns srl";

    //--------------------------------------------------------------------------
    // Fields
    //--------------------------------------------------------------------------

    protected DataModel dataModel;
    protected Map<String, HibernateDatabaseSetup> setups;

    public static final Logger logger =
            LogUtil.getLogger(MDContextHibernateImpl.class);

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public MDContextHibernateImpl() {
    }

    //--------------------------------------------------------------------------
    // Model loading
    //--------------------------------------------------------------------------

    public void loadXmlModelAsResource(String resource) {
        LogUtil.entering(logger, "loadXmlModelAsResource", resource);

        DBParser parser = new DBParser();
        try {
            DataModel loadedDataModel = parser.parse(resource);
            installDataModel(loadedDataModel);
        } catch (Exception e) {
            LogUtil.severeMF(logger, "Cannot load/parse model: {0}", e,
                    resource);
        }

        LogUtil.exiting(logger, "loadXmlModelAsResource");
    }

    private synchronized void installDataModel(DataModel newDataModel) {
        try {
            HashMap<String, HibernateDatabaseSetup> newSetups =
                    new HashMap<String, HibernateDatabaseSetup>();
            for (Database database : newDataModel.getDatabases()) {
                String databaseName = database.getDatabaseName();
                Connection connection = database.getConnection();
                ConnectionProvider connectionProvider =
                        new JdbcConnectionProvider(
                                connection.getDriverClass(),
                                connection.getConnectionUrl(),
                                connection.getUsername(),
                                connection.getPassword());
                DatabaseAbstraction abstraction =
                        DatabaseAbstractionManager.getManager()
                                .getDatabaseAbstraction(connectionProvider);
                HibernateConfig builder = new HibernateConfig(abstraction);
                Configuration configuration =
                        builder.buildSessionFactory(database);
                SessionFactory sessionFactory =
                        configuration.buildSessionFactory();

                HibernateDatabaseSetup setup =
                        new HibernateDatabaseSetup(abstraction,
                                configuration, sessionFactory);
                newSetups.put(databaseName, setup);
            }
            setups = newSetups;
            dataModel = newDataModel;
        } catch (Exception e) {
            LogUtil.severe(logger, "Cannot install model", e);
        }
    }

    //--------------------------------------------------------------------------
    // Database stuff
    //--------------------------------------------------------------------------

    public DatabaseAbstraction getDatabaseAbstraction(String databaseName) {
        return setups.get(databaseName).getDatabaseAbstraction();
    }

    //--------------------------------------------------------------------------
    // Modell access
    //--------------------------------------------------------------------------

    public DataModel getDataModel() {
        return dataModel;
    }

    public void syncDataModel() {
        DataModel syncDataModel = new DataModel();
        try {
            for (Database database : dataModel.getDatabases()) {
                DatabaseAbstraction abstraction =
                        getDatabaseAbstraction(database.getDatabaseName());
                Database syncDatabase =
                        abstraction.readModelFromConnection(
                                database.getDatabaseName());
                syncDatabase.setConnection(database.getConnection());
                syncDataModel.getDatabases().add(syncDatabase);

                installDataModel(syncDataModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    //--------------------------------------------------------------------------
    // Persistance
    //--------------------------------------------------------------------------

    public Map<String, Object> getObjectByPk(String qualifiedTableName,
                                             Object... pk) {
        throw new UnsupportedOperationException();
    }
    
    public Map<String, Object> getObjectByPk(String qualifiedTableName,
                                             HashMap<String, Object> pk) {
        Session session = getSession(qualifiedTableName);

        //noinspection unchecked
        return (Map<String, Object>)session.load(qualifiedTableName, pk);
    }


    public List<Map<String, Object>> getAllObjects(String qualifiedTableName) {
        Session session = getSession(qualifiedTableName);

        Criteria hibernateCriteria = session.createCriteria(qualifiedTableName);
        //noinspection unchecked
        return hibernateCriteria.list();
    }

    protected Session getSession(String qualifiedTableName) {
        Table table = dataModel.findTableByQualifiedName(qualifiedTableName);
        String databaseName = table.getDatabaseName();
        return setups.get(databaseName).getThreadSession();
    }

    public com.manydesigns.elements.fields.search.Criteria
    createCriteria(String qualifiedTableName) {
        Session session = getSession(qualifiedTableName);
        Criteria hibernateCriteria = session.createCriteria(qualifiedTableName);
        return new HibernateCriteriaAdapter(hibernateCriteria);
    }

    public List<Map<String, Object>> getObjects(
            com.manydesigns.elements.fields.search.Criteria criteria) {
        HibernateCriteriaAdapter hibernateCriteriaAdapter =
                (HibernateCriteriaAdapter)criteria;
        Criteria hibernateCriteria =
                hibernateCriteriaAdapter.getHibernateCriteria();
        //noinspection unchecked
        return hibernateCriteria.list();
    }

    public void saveOrUpdateObject(Map<String, Object> obj) {
        Session session = getSession((String) obj.get("$type$"));
        session.beginTransaction();
        session.saveOrUpdate((String) obj.get("$type$"), obj);
        session.getTransaction().commit();
    }

    public void saveObject(Map<String, Object> obj) {
        Session session = getSession((String) obj.get("$type$"));
        session.beginTransaction();
        session.save((String) obj.get("$type$"), obj);
        session.getTransaction().commit();
    }

    public void updateObject(Map<String, Object> obj) {
        Session session = getSession((String) obj.get("$type$"));
        session.beginTransaction();
        session.update((String) obj.get("$type$"), obj);
        session.getTransaction().commit();
    }

    public void deleteObject(Map<String, Object> obj) {
        Session session = getSession((String) obj.get("$type$"));
        session.beginTransaction();
        session.delete((String) obj.get("$type$"), obj);
        session.getTransaction().commit();

    }

    public void openSession() {
        for (HibernateDatabaseSetup current: setups.values()) {
            SessionFactory sessionFactory = current.getSessionFactory();
            Session session = sessionFactory.openSession();
            current.setThreadSession(session);
        }
    }


    public void closeSession() {
        for (HibernateDatabaseSetup current: setups.values()) {
            Session session = current.getThreadSession();
            if (session != null) {
                session.close();
            }
            current.setThreadSession(null);
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<Map<String, Object>> getRelatedObjects(
            Map<String, Object> obj, String oneToManyRelationshipName) {
        if (obj.get(oneToManyRelationshipName) instanceof List){
            return (List<Map<String, Object>>)
                    obj.get(oneToManyRelationshipName);
        }
        String qualifiedTableName = (String)obj.get("$type$");
        Relationship relationship =
                dataModel.findOneToManyRelationship(
                        qualifiedTableName, oneToManyRelationshipName);
        Table fromTable = relationship.getFromTable();

        Session session =
                setups.get(fromTable.getDatabaseName()).getThreadSession();
        Criteria criteria =
                session.createCriteria(fromTable.getQualifiedName());
        for (Reference reference : relationship.getReferences()) {
            Column fromColumn = reference.getFromColumn();
            Column toColumn = reference.getToColumn();
            criteria.add(Restrictions.eq(fromColumn.getColumnName(),
                    obj.get(toColumn.getColumnName())));
        }
        return (List<Map<String, Object>>)criteria.list();
    }
}