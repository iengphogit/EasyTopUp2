package ocr.avaboy.com.data;

import java.util.ArrayList;

import ocr.avaboy.com.model.Company;

/**
 * Created by iengpho on 9/30/18.
 */

public class Singleton {
    private ArrayList<Company> companyArrayList;
    private Company currentCompany;
    private static final Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {}

    public void setCompanyArrayList(ArrayList<Company> list){
        if(companyArrayList == null){
            companyArrayList = new ArrayList<>();
        }
        this.companyArrayList.clear();
        this.companyArrayList.addAll(list);
    }

    public ArrayList<Company> getCompanyArrayList(){
        return this.companyArrayList;
    }


    public Company getCompanyByPosition(int position){
        return companyArrayList.get(position);
    }

    public Company getCurrentCompany() {
        return currentCompany;
    }

    public void setCurrentCompany(Company currentCompany) {
        this.currentCompany = currentCompany;
    }
}
