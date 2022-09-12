import entity.Driver;
import function_handle.BusLineService;
import function_handle.BusRouteManagerService;
import function_handle.DriverService;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainRun {
    public static final DriverService driverService=new DriverService();
    public static final BusLineService busLineService=new BusLineService();
    public static final BusRouteManagerService busRouteManagerService= new BusRouteManagerService();
    public static void main(String[] args) {


//        Session session=HibernateUtil.getSessionFcatory().openSession();
//        Query<Driver> query=session.createQuery("FROM Driver");
//        List<Driver> dri=query.list();
//        dri.forEach(d-> System.out.println(d.getName()));
//        session.close();

        menu();

    }

    private static void menu(){
        while (true){
            showMenu();
            int funcChoice=funcChoice();
            switch (funcChoice){
                case 1:
                    driverService.createNew();
                case 2:
                    driverService.showDrivers();
                case 3:
                    busLineService.createBusLineNew();
                case 4:
                    busLineService.showBusLines();
                case 5:
                    busRouteManagerService.createNew();
                case 6:
                    busRouteManagerService.showAll();
                case 7:
                    busRouteManagerService.sort();

            }

        }
    }

    private static int funcChoice(){
        System.out.println("Xin mời nhập lựa chọn:");
        int choice=-1;
        do {
            try {
                choice=new Scanner(System.in).nextInt();

            }catch (InputMismatchException ip){
                System.out.println("Gía trị nhập phải là số nguyên");
                continue;
            }
            if (choice>=1&&choice<=6){
                return choice;
            }
            System.out.println("Gía trị vừa nhập không hợp lệ, vui lòng nhập lại");
        }while (true);
    }

    private static void showMenu(){
        System.out.println("\n\n\n-------------PHẦN MỀM QUẢN LÝ PHÂN CÔNG LÁI XE BUÝT-------------");
        System.out.println("1. Nhập danh sách lái xe mới.");
        System.out.println("2. Hiển thị danh sách lái xe.");
        System.out.println("3. Nhập danh sách tuyến xe mới.");
        System.out.println("4. Hiển thị danh sách tuyến xe.");
        System.out.println("5. Lập bảng phân công lái xe");
        System.out.println("6. Hiển thị bảng phân công lái xe");
        System.out.println("7. Sắp xếp danh sách phân công");
        System.out.println("8. Lập bảng thống kê khoảng cách chạy xe trong ngày");
        System.out.println("9. Kết thúc chương trình");

    }
}
