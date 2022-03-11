package br.com.udemy.workshopjavafxjdbc.model.services;

import br.com.udemy.workshopjavafxjdbc.model.dao.DaoFactory;
import br.com.udemy.workshopjavafxjdbc.model.dao.DepartmentDao;
import br.com.udemy.workshopjavafxjdbc.model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return departmentDao.findAll();
    }
}
