package function_handle;

import dto.DriverBusManagementDto;
import dto.DriverBusManagementTempDto;
import entity.BusLine;
import entity.BusRouteManager;
import entity.Driver;
import org.hibernate.Session;
import org.hibernate.query.Query;
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


    public void showBusRouteManager(){
        Session session= HibernateUtil.getSessionFactory().openSession();
        Query<BusRouteManager> query=session.createQuery("FROM BusRouteManager ");
        List<BusRouteManager> dri=query.list();
        dri.forEach(d-> System.out.println("Driver Id: "+d.getDriverId()+"\t BusLine Id: "+d.getBusLineId()+"\t S??? tuy???n ch???y:"+d.getRoundNumber()));
        session.close();
    }
    public void createNew(){
//        if(driverService.getDriver().isEmpty()){
//            System.out.println("Ch??a c?? th??ng tin t??i x??? ho???c tuy???n xe, vui l??ng nh???p t??i x??? ho???c tuy???n xe tr?????c.");
//            return;
//        }
        System.out.print("M???i nh???p s??? t??i x??? mu???n ph??n c??ng l??i xe: ");
        int driverNumber =-1;
        do {
            try {
                driverNumber=new Scanner(System.in).nextInt();
            }catch (InputMismatchException ex){
                System.out.println("S??? t??i x??? c???n nh???p ?? m???t s??? nguy??n,vui l??ng nh???p l???i");
                continue;
            }
            if(driverNumber>0){
                break;
            }
            System.out.println("S??? t??i x??? ph???i l?? s??? d????ng,xin m???i nh???p l???i:");
        }while (true);

        List<DriverBusManagementDto> driverBusManagementDtos=new ArrayList<>();

        for (int i = 0; i < driverNumber; i++) {
            System.out.println("Nh???p th??ng tin cho t??i x??? th???"+(i+1)+" : ");
            Driver driver=inputDriver();
            System.out.println("L???p b???ng danh s??ch tuy???n xe l??i trong ng??y c???a l??i xe n??y: ");
            Map<BusLine,Integer> busLineMap=createBusLine();
            DriverBusManagementDto driverBusManagementDto=new DriverBusManagementDto(driver,busLineMap);
            driverBusManagementDto.setTotalDistance();
            driverBusManagementDtos.add(driverBusManagementDto);
        }
        saveAll(toEntity(driverBusManagementDtos));

    }

    ///

    private Map<BusLine, Integer> createBusLine() {
        System.out.print("Nh???p s??? l?????ng tuy???n m?? l??i xe n??y mu???n l??i: ");
        int busLineNumber = -1;
        do {
            try {
                busLineNumber = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("S??? l?????ng tuy???n c???n nh???p l?? m???t s??? nguy??n c?? 5 ch??? s???, vui l??ng nh???p l???i: ");
                continue;
            }
            if (busLineNumber > 0) {
                break;
            }
            System.out.print("S??? l?????ng tuy???n ph???i l?? s??? d????ng, vui l??ng nh???p l???i: ");
        } while (true);
        int totalRound = 0;
        Map<BusLine, Integer> busLineMap = new HashMap<>();
        for (int j = 0; j < busLineNumber; j++) {
            System.out.println("Nh???p m?? tuy???n xe th??? " + (j + 1) + " m?? t??i x??? n??y mu???n l??i: ");
            BusLine busLine;
            do {
                int busLineId = -1;
                do {
                    try {
                        busLineId = new Scanner(System.in).nextInt();
                    } catch (InputMismatchException ex) {
                        System.out.print("M?? tuy???n c???n nh???p l?? m???t s??? nguy??n c?? 3 ch??? s???, vui l??ng nh???p l???i: ");
                        continue;
                    }
                    if (busLineId > 0) {
                        break;
                    }
                    System.out.print("M?? tuy???n ph???i l?? s??? d????ng, vui l??ng nh???p l???i: ");
                } while (true);
                busLine = busLineService.findById(busLineId);
                if (!isEmptyObject(busLine)) {
                    break;
                }
                System.out.println("Kh??ng t??m th???y tuy???n xe c?? m?? " + busLineId + ", vui l??ng nh???p l???i: ");
            } while (true);
            System.out.print("Nh???p s??? l?????t m?? t??i x??? n??y mu???n l??i: ");
            int busRound = -1;
            do {
                try {
                    busRound = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("S??? l?????t c???n nh???p l?? m???t s??? nguy??n, vui l??ng nh???p l???i: ");
                    continue;
                }
                if (busRound > 0) {
                    break;
                }
                System.out.print("S??? l?????t ph???i l?? s??? d????ng, vui l??ng nh???p l???i: ");
            } while (true);
            totalRound += busRound;
            if (totalRound > 15) {
                System.out.println("T??i x??? kh??ng ???????c l??i qu?? 15 l?????t 1 ng??y, d???ng ph??n c??ng t???i ????y.");
                break;
            }
            busLineMap.put(busLine, busRound);
        }
        return busLineMap;
    }


    //

    private Driver inputDriver() {
        Driver driver;
        System.out.println("Nh???p m?? t??i x???");
        do {
            int driverId = -1;
            do {
                try {
                    driverId = new Scanner(System.in).nextInt();
                } catch (InputMismatchException ex) {
                    System.out.print("M?? t??i x??? c???n nh???p l?? m???t s??? nguy??n c?? 5 ch??? s???, vui l??ng nh???p l???i: ");
                    continue;
                }
                if (driverId > 0) {
                    break;
                }
                System.out.print("M?? t??i x??? ph???i l?? s??? d????ng, vui l??ng nh???p l???i: ");
            } while (true);

            driver = driverService.findById(driverId);
            if (!isEmptyObject(driver)) {
                break;
            }
            System.out.println("Kh??ng t??m th???y t??i x??? c?? m?? " + driverId + ", vui l??ng nh???p l???i: ");
        } while (true);
        return driver;
    }

    public void sort() {

        System.out.println("S???p x???p danh s??ch ph??n c??ng l??i xe theo: ");
        System.out.println(" 1. H??? t??n l??i xe");
        System.out.println(" 2. S??? tuy???n ?????m nh???n trong ng??y (gi???m d???n)");
        System.out.print("Vui l??ng nh???p l???a ch???n: ");
        int choice = -1;
        do {
            try {
                choice = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.out.print("Gi?? tr??? c???n nh???p l?? m???t s??? nguy??n, vui l??ng nh???p l???i: ");
                continue;
            }
            if (choice == 1 || choice == 2) {
                break;
            }
            System.out.print("Gi?? tr??? l???a ch???n kh??ng t???n t???i, vui l??ng nh???p l???i: ");
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
        this.showBusRouteManager();
    }

    private void sortByDriverName() {
        this.driverBusManagementDtos.sort(Comparator.comparing(o -> o.getDriver().getName()));
        this.showBusRouteManager();
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
