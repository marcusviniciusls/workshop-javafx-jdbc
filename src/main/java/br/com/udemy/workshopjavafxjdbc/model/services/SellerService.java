package br.com.udemy.workshopjavafxjdbc.model.services;

import br.com.udemy.workshopjavafxjdbc.model.dao.DaoFactory;
import br.com.udemy.workshopjavafxjdbc.model.dao.SellerDao;
import br.com.udemy.workshopjavafxjdbc.model.entities.Seller;

import java.util.List;

public class SellerService {

    private SellerDao sellerDao = DaoFactory.createSellerDao();

    public List<Seller> findAll(){
        return sellerDao.findAll();
    }

    public void saveOrUpdateSeller(Seller seller){
        if (seller.getId() == null){
            sellerDao.insert(seller);
        } else {
            sellerDao.update(seller);
        }
    }

    public void remove(Seller seller){
        sellerDao.deleteById(seller.getId());
    }
}
