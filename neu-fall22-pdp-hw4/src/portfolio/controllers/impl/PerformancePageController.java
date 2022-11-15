package portfolio.controllers.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import portfolio.controllers.PageController;
import portfolio.models.portfolio.PortfolioModel;
import portfolio.views.View;
import portfolio.views.ViewFactory;

public class PerformancePageController implements PageController  {
  private final PortfolioModel portfolioModel;
  private final ViewFactory viewFactory;
  private String errorMessage;

  private Map<LocalDate, Double> map;

  private String portfolioName;

  private LocalDate startDate;
  private LocalDate endDate;

  private List<String>  listStar = new ArrayList<>();
  private List<String> list = new ArrayList<>();
  private String scale;


  public PerformancePageController(PortfolioModel portfolioModel, ViewFactory viewFactory) {
    this.portfolioModel = portfolioModel;
    this.viewFactory = viewFactory;
  }

  private static LocalDate getQuarterEnd(LocalDate localDate) {
    localDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
    long month = localDate.getMonth().getValue();
    if (1 <= month && month <= 3) {
      localDate = localDate.withMonth(3);
      localDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
    } else if (4 <= month && month <= 6) {
      localDate = localDate.withMonth(6);
      localDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
    } else if (7 <= month && month <= 9) {
      localDate = localDate.withMonth(9);
      localDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
    } else {
      localDate = localDate.withMonth(12);
      localDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
    }
    return localDate;
  }

  private String getQuarter(LocalDate monthEnd) {
    if(monthEnd.getMonthValue() >=1 && monthEnd.getMonthValue() <= 3){
      return "1";
    } else if(monthEnd.getMonthValue() >=4 && monthEnd.getMonthValue() <= 6){
      return "2";
    } else if (monthEnd.getMonthValue() >=7 && monthEnd.getMonthValue() <= 9) {
      return "3";
    } else {
      return "4";
    }
  }

  private void updateLists(LocalDate startDate, LocalDate endDate) {
    list = new ArrayList<>();
    listStar = new ArrayList<>();
    scale = null;
    List<Double> listAmount = new ArrayList<>();
    long dayCount = ChronoUnit.DAYS.between(startDate,endDate) + 1;
    long weekCount = ChronoUnit.WEEKS.between(startDate,endDate) + 1;
    long monthCount = ChronoUnit.MONTHS.between(startDate,endDate) + 1 ;
    long yearCount = ChronoUnit.YEARS.between(startDate,endDate) + 1;
    LocalDate currentDate =  startDate;
    if(dayCount <= 30) { // did not finish: how do we know do we had this portfolio now.
      //output the list
      while (!currentDate.isAfter(endDate)) {
        if(map.containsKey(currentDate)) {
          list.add(currentDate + ": ");
          listAmount.add(map.get(currentDate));
          currentDate = currentDate.plusDays(1);
        } else {
          LocalDate currentGet = currentDate.minusDays(1);
          while(!map.containsKey(currentGet)) {
            currentGet = currentGet.minusDays(1);
          }
          list.add(currentDate + ": ");
          listAmount.add(map.get(currentGet));
          currentDate = currentDate.plusDays(1);
        }
      }
    } else if (weekCount <= 23) {
      //split it to weeks
      // find the last working day of this week
      while(!currentDate.isAfter(endDate)) {
        LocalDate currentWeekEnd = currentDate.with(ChronoField.DAY_OF_WEEK, 7);
        LocalDate currentWeekGet = currentDate.with(ChronoField.DAY_OF_WEEK, 5);
        if (currentWeekEnd.isAfter(endDate)) {
          currentWeekEnd = endDate;
          currentWeekGet = endDate;
        }
        while(!map.containsKey(currentWeekGet)) {
          currentWeekGet = currentWeekGet.minusDays(1);
        }
        list.add("Week: " + currentDate + " to " + currentWeekEnd + ": ");
        listAmount.add(map.get(currentWeekGet));
        currentDate = currentWeekEnd.plusDays(1);
      }
    } else if (monthCount <= 30 && monthCount >= 5) {
      //split it to month
      // find the last working day of this month
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentMonthEnd = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        LocalDate currentMonthGet = currentMonthEnd;
        if(currentMonthEnd.isAfter(endDate)) {
          currentMonthEnd = endDate;
          currentMonthGet = endDate;
        }
        while(!map.containsKey(currentMonthGet)) {
          currentMonthGet = currentMonthGet.minusDays(1);
        }
        list.add(currentMonthEnd.getYear() +
                "-" + currentMonthEnd.getMonthValue() + ": ");
        listAmount.add(map.get(currentMonthGet));
        currentDate = currentMonthEnd.plusDays(1);
      }
    } else if ((monthCount / 3) < 29) {
      // split it to quarter
      // find the last working day of this month
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentMonthEnd = getQuarterEnd(currentDate);
        LocalDate currentMonthGet = currentMonthEnd;
        if(currentMonthEnd.isAfter(endDate)) {
          currentMonthEnd = endDate;
          currentMonthGet = endDate;
        }
        while(!map.containsKey(currentMonthGet)) {
          currentMonthGet = currentMonthGet.minusDays(1);
        }
        list.add(currentMonthEnd.getYear() + "-Quarter " + getQuarter(currentMonthEnd)+ ": ");
        listAmount.add(map.get(currentMonthGet));
        currentDate = currentMonthEnd.plusDays(1);
        //monthCount = monthCount -3;
      }
    } else if (yearCount <= 30) {
      //split it to year
      //fins the last working day of this year
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentYearEnd = currentDate.withDayOfYear(currentDate.lengthOfYear());
        LocalDate currentYearGet = currentYearEnd;
        if(currentYearEnd.isAfter(endDate)) {
          currentYearEnd = endDate;
          currentYearGet = endDate;
        }
        while(!map.containsKey(currentYearGet)) {
          currentYearGet = currentYearGet.minusDays(1);
        }
        list.add(currentDate.getYear() + ":");
        listAmount.add(map.get(currentYearGet));
        currentDate = currentYearEnd.plusDays(1);
        //yearCount--;
      }
    } else {
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentYearEnd =
                currentDate.withDayOfYear(currentDate.lengthOfYear()).plusYears(1);
        LocalDate currentYearGet = currentYearEnd;
        if(currentYearEnd.isAfter(endDate)) {
          currentYearEnd = endDate;
          currentYearGet = endDate;
        }
        while(!map.containsKey(currentYearGet)) {
          currentYearGet = currentYearGet.minusDays(1);
        }
        list.add(currentDate.getYear() + " to "
                + currentYearEnd.getYear()+ " : ");
        listAmount.add(map.get(currentYearGet));
        currentDate = currentYearEnd.plusDays(1);
      }
    }

    //
    Double maxAmount =  Collections.max(listAmount);
    Double minAmount =  Collections.min(listAmount);
    if (maxAmount.equals(0.0) && minAmount.equals(0.0)) {
      scale = "all the performance are 0";
      for(int i = 0; i< listAmount.size(); i++) {
        listStar.add("");
      }
      return;
    }
    if(minAmount.equals(0.0)) {
      for(int i = 0; i<listAmount.size(); i++) {
        if(listAmount.get(i).equals(0.0)) {
          continue;
        } else if (minAmount.equals(0.0)) {
          minAmount = listAmount.get(i);
        } else {
          if(listAmount.get(i) < minAmount) {
            minAmount = listAmount.get(i);
          }
        }
      }
    }


    if(maxAmount.equals(minAmount)) {
      scale = "one asterisk is $ " + maxAmount;
      for(int i = 0; i<listAmount.size(); i++) {
        String star ="";
        if(!listAmount.get(i).equals(0.0)) {
          star = star + "*";
        }
        listStar.add(star);
      }
    } else {
      double more = (maxAmount-minAmount) / 45;
      double base = minAmount-more-1;

      scale = "one asterisk is $ " + more + " more than a base amount of $" + base;

      for(int i = 0; i<listAmount.size(); i++) {
        String star ="";
        if(!listAmount.get(i).equals(0.0)) {
          for(int j=0; j < (int) ((listAmount.get(i) - base) / more); j++) {
            star = star + "*";
          }
        }
        listStar.add(star);
      }
    }
  }

  @Override
  public View getView() {
    return viewFactory.newPerformacePageView(portfolioName, startDate, endDate, list,
            listStar, scale, errorMessage);
  }

  @Override
  public PageController handleInput(String input) {
    input = input.trim();
    errorMessage = null;
    portfolioName = portfolioModel.getPortfolio().getName();

    if (input.equals("back")) {
      return new MainPageController(portfolioModel, viewFactory);
    }
    try {
      startDate = null;
      endDate = null;
      String[] cmd = input.split(",");
      if (cmd.length != 2) {
        errorMessage = "Error Format!";
        return this;
      }
      startDate = LocalDate.parse(cmd[0]);
      endDate = LocalDate.parse(cmd[1]);

      if(endDate.isBefore(startDate)) {
        errorMessage = "Error: The start date cannot after the end date!";
      }
      map = portfolioModel.getValues(startDate, endDate);

      // how to know the start date and end date have value?
      if(!map.containsKey(startDate)) {
        errorMessage = "Error: Please choose input new timespan."
                + "This start date maybe the holiday or weekend!";
        startDate = null;
        endDate = null;
        map = new HashMap<>();
        return this;
      }
    }catch (Exception e) {
        errorMessage = "Error Date!";
        startDate = null;
        endDate = null;
        map = new HashMap<>();
        return this;
    }
    updateLists(startDate,endDate);
    map = new HashMap<>();

    /*List<Double> listAmount = new ArrayList<>();
    long dayCount = ChronoUnit.DAYS.between(startDate,endDate) + 1;
    long weekCount = ChronoUnit.WEEKS.between(startDate,endDate) + 1;
    long monthCount = ChronoUnit.MONTHS.between(startDate,endDate) + 1 ;
    long yearCount = ChronoUnit.YEARS.between(startDate,endDate) + 1;
    LocalDate currentDate =  startDate;
    if(dayCount <= 30) { // did not finish: how do we know do we had this portfolio now.
      //output the list
      while (!currentDate.isAfter(endDate)) {
        if(map.containsKey(currentDate)) {
          list.add(currentDate + ": ");
          listAmount.add(map.get(currentDate));
          currentDate = currentDate.plusDays(1);
        } else {
          LocalDate currentGet = currentDate.minusDays(1);
          while(!map.containsKey(currentGet)) {
            currentGet = currentGet.minusDays(1);
          }
          list.add(currentDate + ": ");
          listAmount.add(map.get(currentGet));
          currentDate = currentDate.plusDays(1);
        }
      }
    } else if (weekCount <= 26) {
      //split it to weeks
      // find the last working day of this week
      while(!currentDate.isAfter(endDate)) {
        LocalDate currentWeekEnd = currentDate.with(ChronoField.DAY_OF_WEEK, 7);
        LocalDate currentWeekGet = currentDate.with(ChronoField.DAY_OF_WEEK, 5);
        if (currentWeekEnd.isAfter(endDate)) {
          currentWeekEnd = endDate;
          currentWeekGet = endDate;
        }
        while(!map.containsKey(currentWeekGet)) {
          currentWeekGet = currentWeekGet.minusDays(1);
        }
        list.add("Week: " + currentDate + " to " + currentWeekEnd + ": ");
        listAmount.add(map.get(currentWeekGet));
        currentDate = currentWeekEnd.plusDays(1);
      }
    } else if (monthCount <= 30 && monthCount >= 5) {
      //split it to month
      // find the last working day of this month
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentMonthEnd = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        LocalDate currentMonthGet = currentMonthEnd;
        if(currentMonthEnd.isAfter(endDate)) {
          currentMonthEnd = endDate;
          currentMonthGet = endDate;
        }
        while(!map.containsKey(currentMonthGet)) {
          currentMonthGet = currentMonthGet.minusDays(1);
        }
        list.add(currentMonthEnd.getMonth() + ": ");
        listAmount.add(map.get(currentMonthGet));
        currentDate = currentMonthEnd.plusDays(1);
      }
    } else if ((monthCount / 3) < 29) {
      // split it to quarter
      // find the last working day of this month
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentMonthEnd = getQuarterEnd(currentDate);
        LocalDate currentMonthGet = currentMonthEnd;
        if(currentMonthEnd.isAfter(endDate)) {
          currentMonthEnd = endDate;
          currentMonthGet = endDate;
        }
        while(!map.containsKey(currentMonthGet)) {
          currentMonthGet = currentMonthGet.minusDays(1);
        }
        list.add(currentMonthEnd.getYear() + "-Quarter " + getQuarter(currentMonthEnd)+ ": ");
        listAmount.add(map.get(currentMonthGet));
        currentDate = currentMonthEnd.plusDays(1);
        //monthCount = monthCount -3;
      }
    } else if (yearCount <= 30) {
      //split it to year
      //fins the last working day of this year
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentYearEnd = currentDate.withDayOfYear(currentDate.lengthOfYear());
        LocalDate currentYearGet = currentYearEnd;
        if(currentYearEnd.isAfter(endDate)) {
          currentYearEnd = endDate;
          currentYearGet = endDate;
        }
        while(!map.containsKey(currentYearGet)) {
          currentYearGet = currentYearGet.minusDays(1);
        }
        list.add(currentDate.getYear() + ":");
        listAmount.add(map.get(currentYearGet));
        currentDate = currentYearEnd.plusDays(1);
        //yearCount--;
      }
    } else {
      while (!currentDate.isAfter(endDate)) {
        LocalDate currentYearEnd =
                currentDate.withDayOfYear(currentDate.lengthOfYear()).plusYears(1);
        LocalDate currentYearGet = currentYearEnd;
        if(currentYearEnd.isAfter(endDate)) {
          currentYearEnd = endDate;
          currentYearGet = endDate;
        }
        while(!map.containsKey(currentYearGet)) {
          currentYearGet = currentYearGet.minusDays(1);
        }
        list.add(currentDate.getYear() + " to "
                + currentYearEnd.getYear()+ " : ");
        listAmount.add(map.get(currentYearGet));
        currentDate = currentYearEnd.plusDays(1);
      }
    }

    //
    Double maxAmount =  Collections.max(listAmount);
    Double minAmount =  Collections.min(listAmount);
    if (maxAmount.equals(0.0) && minAmount.equals(0.0)) {
      scale = "all the performance are 0";
      for(int i = 0; i< listAmount.size(); i++) {
        listStar.add("");
      }
      return this;
    }
    if(minAmount.equals(0.0)) {
      for(int i = 0; i<listAmount.size(); i++) {
        if(listAmount.get(i).equals(0.0)) {
          continue;
        } else if (minAmount.equals(0.0)) {
          minAmount = listAmount.get(i);
        } else {
            if(listAmount.get(i) < minAmount) {
              minAmount = listAmount.get(i);
            }
        }
      }
    }


    if(maxAmount.equals(minAmount)) {
      scale = "one asterisk is $ " + maxAmount;
      for(int i = 0; i<listAmount.size(); i++) {
        String star ="";
        if(!listAmount.get(i).equals(0.0)) {
            star = star + "*";
        }
        listStar.add(star);
      }
    } else {
      double more = (maxAmount-minAmount) / 45;
      double base = minAmount-more;

      scale = "one asterisk is $ " + more + "more than a base amount of $" + base;

      for(int i = 0; i<listAmount.size(); i++) {
        String star ="";
        if(!listAmount.get(i).equals(0.0)) {
          for(int j=0; j < (int) ((listAmount.get(i) - base) / more); j++) {
            star = star + "*";
          }
        }
        listStar.add(star);
      }
    }*/
    return this;
  }
}