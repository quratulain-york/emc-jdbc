 package org.eclipse.epsilon.emc.mysql;

import java.sql.SQLException;

import org.eclipse.epsilon.common.module.ModuleElement;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.jdbc.JdbcModel;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.compile.m3.Metamodel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;

import com.mysql.jdbc.Driver;

public class MySqlModel extends JdbcModel {

	public static void main(String[] args) throws Exception {
		MySqlModel model = new MySqlModel();
		model.setServer("useastdb.ensembl.org");
		model.setDatabase("test");
		model.setUsername("anonymous");
		model.setPassword("");
		model.setPort(3306);
		model.setName("M");
		model.load();
		
		EolModule module = new EolModule();
		
		//module.parse("M.find(h:help_topic|h.help_category_id > 12 /*and h.name.startsWith('join')*/);");
		//module.parse("help_topic.all.first().isTypeOf(help_topic).println();");
		//module.parse("for (t in help_topic.all) { (t.help_category_id + 1).println(); }");
		
		//module.parse("var x : new person2; x.name='Mary';");
		// Add support for dates
		//module.parse("transaction {for (p in Person.all) { p.name = 'foo2' + loopCount; } 'foo'.moo();}");
		module.parse("transaction {var p : new Person(Map{'age' = 18});}");
		module.getContext().getModelRepository().addModel(model);
		module.execute();
	}

	@Override
	protected Driver createDriver() throws SQLException {
		return new Driver();
	}

	@Override
	protected String getJdbcUrl() {
		return "jdbc:mysql://" + server + ":" + port + "/" + databaseName;
	}
	
	@Override
	public Metamodel getMetamodel(StringProperties properties, IRelativePathResolver resolver) {
		return super.getMetamodel(properties, resolver);
	}
	
	

}
