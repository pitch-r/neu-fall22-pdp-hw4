package portfolio.mock;

import java.util.Map;
import portfolio.models.portfolio.InflexiblePortfolio;
import portfolio.models.entities.PortfolioWithValue;
import portfolio.views.View;
import portfolio.views.ViewFactory;
import portfolio.views.impl.CreatePageView;
import portfolio.views.impl.InfoPageView;
import portfolio.views.impl.LoadPageView;
import portfolio.views.impl.MainPageView;

/**
 * ViewFactory using ArgumentCaptor.
 */
public class ViewFactoryWithArgumentCaptor implements ViewFactory {

  private final ArgumentCaptor<Object> argumentCaptor;

  public ViewFactoryWithArgumentCaptor(ArgumentCaptor<Object> argumentCaptor) {
    this.argumentCaptor = argumentCaptor;
  }

  @Override
  public View newInfoPageView(PortfolioWithValue portfolioWithPrice,
      String errorMessage) {
    argumentCaptor.addArgument(portfolioWithPrice);
    argumentCaptor.addArgument(errorMessage);
    return new InfoPageView(portfolioWithPrice, errorMessage);
  }

  @Override
  public View newCreatePageView(Boolean isEnd, Boolean isNamed, Map<String, Integer> map,
      String errorMessage) {
    argumentCaptor.addArgument(isEnd);
    argumentCaptor.addArgument(isNamed);
    argumentCaptor.addArgument(map);
    argumentCaptor.addArgument(errorMessage);
    return new CreatePageView(isEnd, isNamed, map, errorMessage);
  }

  @Override
  public View newLoadPageView(InflexiblePortfolio portfolio, String errorMessage) {
    argumentCaptor.addArgument(portfolio);
    argumentCaptor.addArgument(errorMessage);
    return new LoadPageView(portfolio, errorMessage);
  }

  @Override
  public View newMainPageView(String errorMessage, boolean isInitFailed) {
    argumentCaptor.addArgument(errorMessage);
    argumentCaptor.addArgument(isInitFailed);
    return new MainPageView(errorMessage, isInitFailed);
  }

}
