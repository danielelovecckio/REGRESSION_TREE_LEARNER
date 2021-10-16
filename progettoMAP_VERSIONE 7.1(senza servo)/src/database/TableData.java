package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *  Modella l’insieme di transazioni collezionate in una tabella. 
 * 
 */
public class TableData {
	private DbAccess db;
/**
 * 
 * @param db
 */
public TableData(DbAccess db) {
		this.db=db;
}
/**
 * 
 * @param table
 * @return
 * @throws SQLException
 * @throws EmptySetException
 */
public List<Example> getTransazioni(String table) throws SQLException, EmptySetException{
	LinkedList<Example> transSet = new LinkedList<Example>();
	Statement statement;
	TableSchema tSchema=new TableSchema(db,table);
	 
	
	String query="select ";
	
	for(int i=0;i<tSchema.getNumberOfAttributes();i++){
		Column c=tSchema.getColumn(i);
		if(i>0) {
			query+=",";
		}
		query += c.getColumnName();
	}
	if(tSchema.getNumberOfAttributes()==0) {
		throw new SQLException();
	}
	query += (" FROM "+table);
	
	statement = db.getConnection().createStatement();
	ResultSet rs = statement.executeQuery(query);
	boolean empty=true;
	while (rs.next()) {
		empty=false;
		Example currentTuple=new Example();
		for(int i=0;i<tSchema.getNumberOfAttributes();i++)
			if(tSchema.getColumn(i).isNumber()) {
				currentTuple.add(rs.getDouble(i+1));
			}
			else {
				currentTuple.add(rs.getString(i+1));
			}
		transSet.add(currentTuple);
	}
	rs.close();
	statement.close();
	if(empty) throw new EmptySetException();
	
	return transSet;

	}

/**
 * Formula ed esegue un' interrogazione SQL per estrarre
 * i valori distinti ordinati di column che assume nella tabella identificata dal nome table
 * @param table	
 * @param column
 * @return Set
 * @throws SQLException
 */
public Set<Object> getDistinctColumnValues(String table, Column column)throws SQLException{
	Set<Object> ts = new TreeSet<Object>();
	Statement statement = db.getConnection().createStatement();
	String query = "SELECT DISTINCT " + column.getColumnName() + " FROM " + table;
	ResultSet rs = statement.executeQuery(query);
	if (column.isNumber())
		while (rs.next())
			ts.add(rs.getDouble(column.getColumnName()));
	else
		while (rs.next())
			ts.add(rs.getString(column.getColumnName()));
	return ts;
	}

enum QUERY_TYPE {
MIN, MAX
}

}
