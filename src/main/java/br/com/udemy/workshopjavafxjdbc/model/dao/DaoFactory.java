package br.com.udemy.workshopjavafxjdbc.model.dao;

import br.com.udemy.workshopjavafxjdbc.db.DB;
import br.com.udemy.workshopjavafxjdbc.model.dao.impl.DepartmentDaoJDBC;
import br.com.udemy.workshopjavafxjdbc.model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
