Feature: Market order

  Scenario: A market buy order is placed
    Given A limit sell order exists in the order book for 700 shares at 100
    When A market buy order is placed for 600 shares
    Then There should be 100 shares left in the order book
