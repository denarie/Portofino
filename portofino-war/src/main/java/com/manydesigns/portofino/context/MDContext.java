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

package com.manydesigns.portofino.context;

import com.manydesigns.elements.fields.search.Criteria;
import com.manydesigns.portofino.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
*/
public interface MDContext {
    public static final String copyright =
            "Copyright (c) 2005-2010, ManyDesigns srl";

    //--------------------------------------------------------------------------
    // Model loading
    //--------------------------------------------------------------------------

    void loadXmlModelAsResource(String resource);


    //--------------------------------------------------------------------------
    // Model access
    //--------------------------------------------------------------------------

    List<Table> getAllTables();

    Database findDatabaseByName(String databaseName)
            throws ModelObjectNotFoundException;

    Schema findSchemaByQualifiedName(String qualifiedSchemaName)
            throws ModelObjectNotFoundException;

    Table findTableByQualifiedName(String qualifiedTableName)
            throws ModelObjectNotFoundException;

    Column findColumnByQualifiedName(String qualifiedColumnName)
            throws ModelObjectNotFoundException;

    Relationship findOneToManyRelationship(Table table, String relationshipName)
            throws ModelObjectNotFoundException;


    //--------------------------------------------------------------------------
    // Persistance
    //--------------------------------------------------------------------------

    Map<String, Object> getObjectByPk(String qualifiedTableName,
                                      Object... pk);

    Map<String, Object> getObjectByPk(String qualifiedTableName,
                                      HashMap<String, Object> pk);

    List<Map<String, Object>> getAllObjects(String qualifiedTableName);

    Criteria createCriteria(String qualifiedTableName);

    List<Map<String, Object>> getObjects(Criteria criteria);

    void saveOrUpdateObject(Map<String, Object> obj);

    void saveObject(Map<String, Object> obj);

    void updateObject(Map<String, Object> obj);

    void deleteObject(Map<String, Object> obj);

    void openSession();

    void closeSession();

    List<Map<String, Object>> getRelatedObjects(
            Map<String, Object> obj, String oneToManyRelationshipName);
}