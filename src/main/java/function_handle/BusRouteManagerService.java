package function_handle;

import dto.DriverBusManagementDto;
import dto.DriverBusManagementTempDto;
import entity.BusLine;
import entity.BusRouteManager;
import entity.Driver;
import org.hibernate.Session;
import util.HibernateUtil;

import java.util.*;
import java.util.stream.Collectors;

public class BusRouteManagerService {
    DriverService driverService=new DriverService();
    BusLineService busLineService=new BusLineService();

//    private final DriverBusManagementRepository driverBusManagementRepository = new DriverBusManagementRepository();

    private List<BusRouteManager> busRouteManagers=new ArrayList<>();

    private List<DriverBusManagementDto> driverBusManagementDtos;
    public   List<DriverBusManagementDto> getDriverBusManagementDtos(){
        return driverBusManagementDtos;
    }

    public void setDriverBusManagementDtos(List<DriverBusManagementDto> driverBusManagementDtos){
        this.driverBusManagementDtos=driverBusManagementDtos;
    }


    private  List<DriverBusManagementDto> toDto(List<BusRouteManager> busRouteManagers){
        List<DriverBusManagementTempDto> driverBusManagementTempDtos=new ArrayList<>();
        busRouteManagers.forEach(busRouteManager -> {
            double driverId=busRouteManager.getDriverId();
            Driver driver=driverService.findById(Math.toIntExact((long) driverId));
            double busLineId=busRouteManager.getBusLineId();
            BusLine busLine=busLineService.findById(Math.toIntExact((long) busLineId));
            Integer roundNumber=busRouteManager.getRoundNumber();
            driverBusManagementTempDtos.add(new DriverBusManagementTempDto(driver,busLine,roundNumber));
        });

        Map<Driver,Map<BusLine,Integer>> tempMap=driverBusManagementTempDtos
                .stream()
                .collect(
                        Collectors.groupingBy(
                                DriverBusManagementTempDto::getDriver,
                                Collectors.toMap(DriverBusManagementTempDto::getBusLine,DriverBusManagementTempDto::getRound)
                        )
                );
        final List<DriverBusManagementDto> result=new ArrayList<>();
        tempMap.forEach((key,value)->{
            DriverBusManagementDto driverBusManagementDto=new DriverBusManagementDto(key,value);
            driverBusManagementDto.setTotalDistance();
            result.add(driverBusManagementDto);

        });
        return result;
    }

    public List<BusRouteManager> toEntity(List<DriverBusManagementDto> driverBusManagementDtos){
        final List<BusRouteManager> busRouteManagers=new ArrayList<>();
        driverBusManagementDtos.forEach(management ->{
            Driver driver=management.getDriver();
            management.getAssignedBuses().forEach((key,value)->{
                BusRouteManager temp=new BusRouteManager();
                temp.setDriverId(driver.getId());
                temp.setBusLineId(key.getId());
                temp.setRoundNumber(value);
                busRouteManagers.add(temp);
            });
        });
        return busRouteManagers;
    }

    public void showAll() {
        this.driverBusManagementDtos.forEach(System.out::println);
    }


    public void createNew(){
        if(driverService.getDriver().isEmpty()){
            System.out.println("Chưa có thông tin tài xế hoặc tuyến xe, vui lòng nhập tài xế hoặc tuyến xe trước.");
            return;
        }
        System.out.print("Mời nhập số tài xế muốn phân công lái xe: ");
        int driverNumber =-1;
        do {
            try {
                driverNumber=new Scanner(System.in).nextInt();
            }catch (InputMismatchException ex){
                System.out.println("Số tài xế cần nhập à một số nguyên,vui lòng nhập lại");
                continue;
            }
            if(driverNumber>0){
                break;
            }
            System.out.println("Số tài xế phải là số dương,xin mời nhập lại:");
        }while (true);

        List<DriverBusManagementDto> driverBusManagementDtos=new ArrayList<>();

        for (int i = 0; i < driverNumber; i++) {
            System.out.println("Nhập thông tin cho tài xế thứ"+(i+1)+" : ");
            Driver driver=inputDriver();
            System.out.println("Lập bảng danh sách tuyến xe lái trong ngày của lái xe này: ");
            Map<BusLine,Integer> busLineMap=createBusLine();
            DriverBusManagementDto driverBusManagementDto=new DriverBusManagementDto(driver,busLineMap);
            driverBusManagementDto.setTotalDistance();
            driverBusManagementDtos.add(driverBusManagementDto);
        }
        saveAll(toEntity(driverBusManagementDtos));

    }

    ///

    private Map<BusLine, Integer> createBusLine() {
        System.out.print("Nhập số lượng tuyến mà lái xe này muốn lái: ");
        int busLineNumber = -1;
        do {
            try {
                busLineNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Số lượng tuyến cần nhập là một số nguyên có 5 chữ số, vui lòng nhập lại: ");
                continue;
            }
            if (busLineNumber > 0) {
                break;
            }
            System.out.print("Số lượng tuyến phải là số dương, vui lòng nhập lại: ");
        } while (true);
        int totalRound = 0;
        Map<BusLine, Integer> busLineMap = new HashMap<>();
        for (int j = 0; j < busLineNumber; j++) {
            System.out.println("Nhập mã tuyến xe thứ " + (j + 1) + " mà tài xế này muốn lái: ");
            BusLine busLine;
            do {
                int busLineId = -1;
                do {
                    try {
                        busLineId = new Scanner(System.in).nextInt();
                    } catch (InputMismatchException ex) {
                        System.out.print("Mã tuyến cần nhập là một số nguyên có 3 chữ số, vui lòng nhập lại: ");
                        continue;
                    }
                    if (busLineId > 0) {
                        break;
                    }
                    System.out.print("Mã tuyến phải là số dương, vui lòng nhập lại: ");
                } while (true);
                busLine = busLineService.findById(busLineId);
                if (!isEmptyObject(busLine)) {
                    break;
                }
                System.out.println("Không tìm thấy tuyến xe có mã " + busLineId + ", vui lòng nhập lại: ");
            } while (true);
            System.out.print("Nhập số lượt mà tài xế này muốn lái: ");
            int busRound = -1;
            do {
                try {
                    busRound = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("Số lượt cần nhập là một số nguyên, vui lòng nhập lại: ");
                    continue;
                }
                if (busRound > 0) {
                    break;
                }
                System.out.print("Số lượt phải là số dương, vui lòng nhập lại: ");
            } while (true);
            totalRound += busRound;
            if (totalRound > 15) {
                System.out.println("Tài xế không được lái quá 15 lượt 1 ngày, dừng phân công tại đây.");
                break;
            }
            busLineMap.put(busLine, busRound);
        }
        return busLineMap;
    }


    //

    private Driver inputDriver() {
        Driver driver;
        System.out.println("Nhập mã tài xế");
        do {
            int driverId = -1;
            do {
                try {
                    driverId = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("Mã tài xế cần nhập là một số nguyên có 5 chữ số, vui lòng nhập lại: ");
                    continue;
                }
                if (driverId > 0) {
                    break;
                }
                System.out.print("Mã tài xế phải là số dương, vui lòng nhập lại: ");
            } while (true);

            driver = driverService.findById(driverId);
            if (!isEmptyObject(driver)) {
                break;
            }
            System.out.println("Không tìm thấy tài xế có mã " + driverId + ", vui lòng nhập lại: ");
        } while (true);
        return driver;
    }

    public void sort() {

        System.out.println("Sắp xếp danh sách phân công lái xe theo: ");
        System.out.println(" 1. Họ tên lái xe");
        System.out.println(" 2. Số tuyến đảm nhận trong ngày (giảm dần)");
        System.out.print("Vui lòng nhập lựa chọn: ");
        int choice = -1;
        do {
            try {
                choice = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Giá trị cần nhập là một số nguyên, vui lòng nhập lại: ");
                continue;
            }
            if (choice == 1 || choice == 2) {
                break;
            }
            System.out.print("Giá trị lựa chọn không tồn tại, vui lòng nhập lại: ");
        } while (true);

        switch (choice) {
            case 1:
                sortByDriverName();
                break;
            case 2:
                sortByBusLineNumber();
                break;
        }
    }

    private void sortByBusLineNumber() {
        this.driverBusManagementDtos.sort(Comparator.comparing(DriverBusManagementDto::getTotalDistance).reversed());
        this.showAll();
    }

    private void sortByDriverName() {
        this.driverBusManagementDtos.sort(Comparator.comparing(o -> o.getDriver().getName()));
        this.showAll();
    }


    public static boolean isEmptyObject(Object obj) {
        return obj == null;
    }

    public static boolean isEmptyCollection(Collection<?> collection) {
        return isEmptyObject(collection) || collection.isEmpty();
    }

    public void saveAll(List<BusRouteManager> toEntity) {
        if (isEmptyCollection(toEntity)) {
            return;
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.getTransaction().begin();
        try {
            for (BusRouteManager driverBusManagement : toEntity) {
                session.save(driverBusManagement);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }
        session.getTransaction().commit();
        session.close();
    }

}
