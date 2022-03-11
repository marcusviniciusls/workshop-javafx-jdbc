package br.com.udemy.workshopjavafxjdbc.services;

import br.com.udemy.workshopjavafxjdbc.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll(){
        List<Department> listDepartment = new ArrayList<>();
        listDepartment.add(new Department(1, "Books"));
        listDepartment.add(new Department(2, "Computers"));
        listDepartment.add(new Department(3, "Eletronics"));
        return listDepartment;
    }
}
