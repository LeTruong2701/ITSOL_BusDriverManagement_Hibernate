package function_handle;

import entity.BusLine;
import entity.Driver;
import org.hibernate.Session;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BusLineService {
    private List<BusLine> busLines=new ArrayList<>();

    public List<BusLine> getBusLines(){
        return busLines;
    }

    public void setBusLines(List<BusLine> busLines){
        this.busLines=busLines;
    }

    public void showBusLines(){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query<BusLine> query=session.createQuery("FROM BusLine ");
        List<BusLine> dri=query.list();
        dri.forEach(d-> System.out.println("Mã tuyến đường: "+d.getId()+" Khoảng cách: "+d.getDistance()+"\t Điểm dừng: "+d.getStopStationNumber()));
        session.close();
    }

    public void createBusLineNew(){
        System.out.print("Mời nhập số tuyến xe muốn thêm mới: ");
        int busLineNumber= -1;
        do {
            try {
                busLineNumber=new Scanner(System.in).nextInt();
            }catch (InputMismatchException ip){
                System.out.println("Cần nhập số nguyên, vui lòng nhập lại");
                continue;
            }
            if (busLineNumber>0){
                break;
            }
            System.out.println("Số tuyến xe cần nhập số dương,vui lòng nhập lại: ");
        }while(true);

//        List<Driver> newDrivers=new ArrayList<>();
        for (int i = 0; i < busLineNumber; i++) {
            BusLine busLine=inputInfo();
            this.busLines.add(busLine);
//            newDrivers.add(driver);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
            session.getTransaction().begin();
            try {
                for (BusLine busLine : busLines) {
                    session.save(busLine);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                session.getTransaction().rollback();
            }
            session.getTransaction().commit();


    }

    public BusLine inputInfo(){
        BusLine busLine=new BusLine();
        System.out.print("Nhập Khoảng cách tuyến xe: ");
        double distance=-1;
        do {

            try {
                 distance=new Scanner(System.in).nextDouble();
            }catch (InputMismatchException ip){
                System.out.println(ip);
            }
            if(distance>0){
                break;
            }
            System.out.println("Khoảng cách tuyến xe phải là số dương,vui lòng nhập lại:");
        }while (true);
        busLine.setDistance(distance);

        System.out.print("Nhập số điểm dừng:");
        int stopStationNumber=-1;
        do {
            try {
                stopStationNumber=new Scanner(System.in).nextInt();
            }catch (InputMismatchException ip){
                System.out.println("Số điểm dừng là số nguyên,vui lòng nhập lại");
                continue;
            }
            if(stopStationNumber>0){
                break;
            }
            System.out.print("Số điểm dừng phải là số nguyên dương,vui long nhập lại: ");
        }while (true);
        busLine.setStopStationNumber(stopStationNumber);
        return busLine;
    }


    public BusLine findById(int busLineId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (BusLine) session.createQuery("FROM BusLine where id = :p_id")
                .setParameter("p_id", busLineId)
                .getSingleResult();

    }

}
