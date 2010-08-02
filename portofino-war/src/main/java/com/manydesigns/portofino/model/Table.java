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

package com.manydesigns.portofino.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
*/
public class Table {
    public static final String copyright =
            "Copyright (c) 2005-2010, ManyDesigns srl";

    //--------------------------------------------------------------------------
    // Fields
    //--------------------------------------------------------------------------

    protected String databaseName;
    protected String schemaName;
    protected String tableName;
    protected final List<Column> columns;
    protected final List<Relationship> manyToOneRelationships;
    protected final List<Relationship> oneToManyRelationships;
    protected PrimaryKey primaryKey;


    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------
    public Table(String databaseName, String schemaName, String tableName) {
        this.databaseName = databaseName;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columns = new ArrayList<Column>();
        this.manyToOneRelationships = new ArrayList<Relationship>();
        this.oneToManyRelationships = new ArrayList<Relationship>();
     }

    //--------------------------------------------------------------------------
    // Getters/setter
    //--------------------------------------------------------------------------

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public List<Relationship> getManyToOneRelationships() {
        return manyToOneRelationships;
    }

    public List<Relationship> getOneToManyRelationships() {
        return oneToManyRelationships;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getQualifiedName() {
        return MessageFormat.format("{0}.{1}.{2}",
                databaseName, schemaName, tableName);
    }

    //--------------------------------------------------------------------------
    // toString()
    //--------------------------------------------------------------------------

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("databaseName", databaseName)
                .append("schemaName", schemaName)
                .append("tableName", tableName)
                .toString();
    }
}