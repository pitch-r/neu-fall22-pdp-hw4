package portfolio.services.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import portfolio.entities.Portfolio;
import portfolio.services.IOService;
import portfolio.services.PortfolioService;

public class PortfolioServiceImpl implements PortfolioService {
  private IOService ioService;

  public PortfolioServiceImpl(IOService ioService){
    this.ioService = ioService;
  }

  @Override
  public Portfolio getPortfolio(String fileName){
    try {
      String str = ioService.read(fileName);
      return parse(str);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean saveToFile(Portfolio portfolio, String fileName) {
    String str = toString(portfolio);
    try {
      return ioService.saveTo(str, fileName);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }


  private static Portfolio parse(String str) {

    PortfolioBuilder builder = new PortfolioBuilder();

    for (String line: str.split("\n")) {
      String[] stock = line.split(",");
      builder.add(stock[0], Integer.parseInt(stock[1]));
    }

    return builder.getResult();
  }

  private static String toString(Portfolio portfolio) {
    Map<String, Integer> map = portfolio.getStockMap();
    StringBuilder builder = new StringBuilder();

    for (Map.Entry<String,Integer> entry: map.entrySet())
    {
      builder.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
    }

    return builder.toString();
  }
}
