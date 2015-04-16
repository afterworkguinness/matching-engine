Feature: Market order

  Scenario: A market buy order is placed
    Given A limit sell order exists in the order book for 700 shares at 100
    When A market buy order is placed for 600 shares
    Then The sell side of the order book should look like this after the trade is executed:
      | side | quantity | limitPrice |
      | sell | 100      | 100        |

  Scenario: A market buy order is placed and matched by multiple limit orders
    Given these limit orders in the order book
      | side | quantity | limitPrice |
      | sell | 500      | 100        |
      | sell | 400      | 100        |
    When A market buy order is placed for 600 shares
    Then The sell side of the order book should look like this after the trade is executed:
      | side | quantity | limitPrice |
      | sell | 300      | 100        |

  Scenario: A market buy order is converted to a limit order when liquidity runs out
    Given these limit orders in the order book
      | side | quantity | limitPrice |
      | sell | 500      | 100        |
      | sell | 100      | 200        |
    When A market buy order is placed for 700 shares
    Then The buy side of the order book should look like this after the trade is executed:
      | side | quantity | limitPrice |
      | buy  | 100      | 200        |
