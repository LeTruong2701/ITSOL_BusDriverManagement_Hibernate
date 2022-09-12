package function_handle;

import entity.Driver;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class DriverService {

    private List<Driver> drivers = new ArrayList<>();

//    private final DriverRespository driverRespository=new DriverRespository();

    public List<Driver> getDriver(){
        return drivers;
    }

    public void setDrivers(List<Driver> drivers){
        this.drivers=drivers;
    }

//    @Override
//    public void init(){
//        this.setDrivers(driverRepository.getAll());
//    }

    public void showAll(){
        this.drivers.forEach(System.out::println);
    }

    public void showDrivers(){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query<Driver> query=session.createQuery("FROM Driver");
        List<Driver> dri=query.list();
        dri.forEach(d-> System.out.println("Họ tên: "+d.getName()+"\t Địa chỉ: "+d.getAddress()+"\t SĐT:"+d.getPhoneNumber()+"\t Trình độ:"+d.getDriverLevel()));
        session.close();
    }

    public void createNew(){
        System.out.print("Mời nhập số tài xế muốn thêm mới: ");
        int driverNumber= -1;
        do {
            try {
                driverNumber=new Scanner(System.in).nextInt();
            }catch (InputMismatchException ip){
                System.out.println("Cần nhập số nguyên, vui lòng nhập lại");
                continue;
            }
            if (driverNumber>0){
                break;
            }
            System.out.println("số tài xế cần nhập số dương,vui lòng nhập lại: ");
        }while(true);

//        List<Driver> newDrivers=new ArrayList<>();
        for (int i = 0; i < driverNumber; i++) {
            Driver driver=inputInfo();
            this.drivers.add(driver);
//            newDrivers.add(driver);

        }

        Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            try {
                for (Driver driver : drivers) {
                    session.save(driver);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                session.getTransaction().rollback();
            }
            session.getTransaction().commit();
            session.close();


    }


    public Driver inputInfo(){
        Driver driver=new Driver();
        System.out.print("Nhập họ tên tài xế: ");
        driver.setName(new Scanner(System.in).nextLine());
        System.out.print("Nhập địa chỉ tài xế:");
        driver.setAddress(new Scanner(System.in).nextLine());
        System.out.print("Nhâp SĐT tài xế: ");
        driver.setPhoneNumber(new Scanner(System.in).nextLine());
        driver.setDriverLevel(this.driverLevel());
        return driver;
    }

    public String driverLevel(){
        System.out.println("Nhập trình độ tài xế chọn 1 trong các trình đồ dưới:");
        System.out.println("1. Loại A");
        System.out.println("2. Loại B");
        System.out.println("3. Loại C");
        System.out.println("4. Loại D");
        System.out.println("5. Loại E");
        System.out.println("6. Loại F");
        int choice=-1;
        do {
            try {
                choice=new Scanner(System.in).nextInt();
            }catch (InputMismatchException ip){
                System.out.println("Gía trị cần nhập là 1 số nguyên");
                continue;
            }
            if(choice>=1 && choice<=6){
                break;
            }
            System.out.println("Gía trị lựa chọn không tồn tại,vui lòng nhập lại: ");
        }while (true);

        switch (choice){
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            case 5: return "E";
            case 6: return "F";
        }
        return null;
    }


//    public Driver findById(int driverId) {
//        return driverRepository.findById(driverId);
//    }

    public Driver findById(int driverId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (Driver) session.createQuery("From Driver where id = :p_id")
                .setParameter("p_id", driverId)
                .getSingleResult();
    }

}
