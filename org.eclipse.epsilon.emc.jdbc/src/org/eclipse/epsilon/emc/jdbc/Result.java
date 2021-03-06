/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 ******************************************************************************/
package org.eclipse.epsilon.emc.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.epsilon.eol.models.IModelElement;

public class Result implements IModelElement {
	
	protected ResultSet resultSet;
	protected int row = -1;
	protected JdbcModel model;
	protected Table table;
	protected Map<String, Object> cache = new HashMap<>();
	protected boolean streamed = false;
	
	public Result(ResultSet rs, JdbcModel model, Table table, boolean streamed) {
		this.resultSet = rs;
		this.model = model;
		this.table = table;
		this.streamed = streamed;
		cacheValues();
	}
	
	public Result(ResultSet rs, int row, JdbcModel model, Table table, boolean streamed) {
		this.resultSet = rs;
		this.row = row;
		this.model = model;
		this.table = table;	
		this.streamed = streamed;
		cacheValues();
	}
	
	public Result(Map<String, Object> cache, JdbcModel model, Table table, boolean streamed) {
		this.model = model;
		this.table = table;
		this.streamed = streamed;
		this.cache = cache;
	}
	
	protected void cacheValues() {
		
		if (!streamed) return;
		
		try {
			ResultSetMetaData metadata = resultSet.getMetaData();
			if(resultSet.next()) {
				for (int i = 1; i<= metadata.getColumnCount(); i++) {
					cache.put(metadata.getColumnName(i).toLowerCase(), resultSet.getObject(i));
				}
			}
		}
		catch (Exception ex) { throw new RuntimeException(ex); }
	}
	
	
	public int getRow() {
		return row;
	}
	
	protected Object getValue(String name) throws SQLException {
		
		if (streamed) {
			return cache.get(name.toLowerCase());
		}
		else {
			//int oldRow = resultSet.getRow();
			if (row != -1) resultSet.absolute(row);
			return resultSet.getObject(name);
			//if (oldRow > 0) resultSet.absolute(oldRow);
		}
	}
	
	protected void setValue(String name, Object value) throws SQLException {
		
		if (streamed) {
			throw new UnsupportedOperationException();
		}
		else {
			//int oldRow = resultSet.getRow();
			if (row != -1) resultSet.absolute(row);
			resultSet.updateObject(name, value);
			resultSet.updateRow();
			//if (oldRow > 0) resultSet.absolute(oldRow);	
		}
	}
	
	@Override
	public JdbcModel getOwningModel() {
		return model;
	}
	
	public ResultSet getResultSet() {
		return resultSet;
	}
	
	public Table getTable() {
		return table;
	}

	protected int getColumnCount() throws SQLException {
		return resultSet.getMetaData().getColumnCount();
	}
	
	@Override
	public String toString() {
		return getClass()+"#"+hashCode()+" : "+table.getName()+" : "+ row + " : "+cache;		
	}
}
