package portfolio.controllers.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import portfolio.controllers.PageController;
import portfolio.controllers.PageControllerFactory;
import portfolio.entities.Portfolio;
import portfolio.entities.StockListEntry;
import portfolio.services.portfolio.PortfolioService;
import portfolio.services.stockprice.StockQueryService;
import portfolio.views.ViewFactory;
import portfolio.views.View;

/**
 * This is a page controller for the create page, which is implement the page controller.
 * CreatePageController handles input from user and is responsible for checking valid stock input,
 * creating portfolio, saving portfolio and generate View. The controller can hold states while user
 * creating their portfolio. The states are stock selection, naming portfolio and portfolio
 * confirmation.
 */
public class CreatePageController implements PageController {

  private final StockQueryService stockQueryService;
  private final PortfolioService portfolioService;
  private final PageControllerFactory pageControllerFactory;
  private final ViewFactory viewFactory;
  private String errorMessage;
  private Boolean isEnd = false;
  private Boolean isNamed = false;
  private Portfolio portfolio;
  private final Map<String, Integer> stockList = new HashMap<>();

  /**
   * This is a constructor to construct a CreatePageController.
   *
   * @param stockQueryService the stock price data that we get from the external source
   * @param portfolioService  the portfolio service
   * @param controllerFactory PageControllerFactory for creating PageController
   * @param viewFactory ViewFactor for creating a view
   */
  public CreatePageController(StockQueryService stockQueryService,
      PortfolioService portfolioService, PageControllerFactory controllerFactory,
      ViewFactory viewFactory) {
    this.stockQueryService = stockQueryService;
    this.portfolioService = portfolioService;
    this.pageControllerFactory = controllerFactory;
    this.viewFactory = viewFactory;
  }

  @Override
  public View getView() {
    return viewFactory.newCreatePageView(isEnd, isNamed, stockList, errorMessage);
  }

  /**
   * Handle user input for creating portfolio. User can select stock symbol and number of shares,
   * after type 'end', user can input the desire portfolio name. Portfolio name cannot be the same
   * as an existing file in the folder and some keywords such as 'end', 'yes' and 'no'. The method
   * return the next page controller that user should be navigated to.
   *
   * @param input user input as a string
   * @return PageController as a next page to be redirected
   */
  @Override
  public PageController handleInput(String input) {
    input = input.trim();
    errorMessage = null;
    if (input.equals("back")) {
      return pageControllerFactory.newMainPageController();
    }
    if (!isEnd && !input.equals("end")) {
      List<String> allStocks = new ArrayList<>();

      try {
        List<StockListEntry> stockList = stockQueryService.getStockList();
        for (StockListEntry entry : stockList) {
          allStocks.add(entry.getSymbol());
        }
      } catch (Exception e) {
        errorMessage = "External API is not ready. Please try again in the next few minutes.";
      }

      try {
        String[] cmd = input.split(",");
        String symbol = cmd[0];
        if (cmd.length != 2) {
          errorMessage = "Error Format!";
          return this;
        }
        if (allStocks.contains(symbol)) {
          int amount = 0;
          try {
            amount = Integer.parseInt(cmd[1]);
          } catch (Exception e) {
            errorMessage = "The share is not a number.";
            return this;
          }
          if (amount <= 0) {
            errorMessage = "The shares cannot be negative and 0.";
            return this;
          }
          stockList.put(symbol, stockList.getOrDefault(symbol, 0) + amount);
        } else {
          errorMessage = "Symbol not found.";
          return this;
        }
      } catch (Exception e) {
        errorMessage = "error!";
        return this;
      }
    } else if (input.equals("end") && stockList.size() == 0) {
      errorMessage = "No stock entered. Please input stock.";
      return this;
    } else if (input.equals("end") && !isEnd && !isNamed) {
      portfolio = new Portfolio(stockList);
      isEnd = true;
      return this;
    } else if (isEnd && !isNamed) {
      //save to file
      if (input.equals("end") || input.equals("yes") || input.equals("no")) {
        errorMessage = "The name cannot be end, back, no and yes.";
        return this;
      }
      try {
        isNamed = portfolioService.saveToFile(portfolio, input + ".txt");
      } catch (Exception e) {
        errorMessage = e.getMessage();
      }
      return this;
    } else {
      if (input.equals("yes")) {
        return pageControllerFactory.newInfoPageController(portfolio);
      } else {
        return pageControllerFactory.newMainPageController();
      }
    }
    return this;
  }

}
