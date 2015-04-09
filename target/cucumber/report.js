$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("com/infusion/trading/matching/marketOrder.feature");
formatter.feature({
  "line": 1,
  "name": "Market order",
  "description": "",
  "id": "market-order",
  "keyword": "Feature"
});
formatter.scenario({
  "line": 3,
  "name": "A market buy order is placed",
  "description": "",
  "id": "market-order;a-market-buy-order-is-placed",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 4,
  "name": "A limit sell order exists in the order book for 700 shares at 100",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "A market buy order is placed for 600 shares",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "There should be 100 shares left in the order book",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "sell order exists in the order",
      "offset": 8
    },
    {
      "val": "700",
      "offset": 48
    },
    {
      "val": "100",
      "offset": 62
    }
  ],
  "location": "MarketOrderSteps.addLimitOrderToOrderBook(String,int,int)"
});
formatter.result({
  "duration": 76230148,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "buy",
      "offset": 9
    },
    {
      "val": "600",
      "offset": 33
    }
  ],
  "location": "MarketOrderSteps.addMarketOrder(String,int)"
});
formatter.result({
  "duration": 76027,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "0",
      "offset": 18
    }
  ],
  "location": "MarketOrderSteps.doNothing(int)"
});
formatter.result({
  "duration": 1614437,
  "error_message": "java.lang.AssertionError: expected:\u003c200\u003e but was:\u003c100\u003e\r\n\tat org.junit.Assert.fail(Assert.java:88)\r\n\tat org.junit.Assert.failNotEquals(Assert.java:834)\r\n\tat org.junit.Assert.assertEquals(Assert.java:645)\r\n\tat org.junit.Assert.assertEquals(Assert.java:631)\r\n\tat com.infusion.trading.matching.MarketOrderSteps.doNothing(MarketOrderSteps.java:24)\r\n\tat ✽.Then There should be 100 shares left in the order book(com/infusion/trading/matching/marketOrder.feature:6)\r\n",
  "status": "failed"
});
});