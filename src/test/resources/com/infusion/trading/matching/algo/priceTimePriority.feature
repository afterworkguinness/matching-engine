Feature: Price Time Priority

  Scenario: A sell limit order is added to the order book at a position according to price time priority
    Given The order book looks like this before the trade is executed:
      | side | quantity | limitPrice |
      | sell | 100      | 200        |
      | sell | 150      | 250        |
    When A sell limit order is placed for 150 shares at 100
    Then The sell side of the order book should look like this:
      | side | quantity | limitPrice |
      | sell | 150      | 100        |
      | sell | 100      | 200        |
      | sell | 150      | 250        |

  Scenario: A buy limit order is added to the buy side order book at a position according to price time priority
    Given The order book looks like this before the trade is executed:
      | side	| quantity | limitPrice |
      | buy		| 100      | 250        |
      | buy		| 150      | 200        |
    When A buy limit order is placed for 150 shares at 100
    Then The buy side of the order book should look like this:
      | side	| quantity | limitPrice |
      | buy		| 100      | 250        |
      | buy		| 150      | 200        |
      | buy		| 150      | 100        |

  Scenario: A limit order is added to the order book while another order exists for the same price
    Given The order book looks like this before the trade is executed:
      | side | quantity | limitPrice | 
      | sell | 100      | 200        |
      | sell | 150      | 250        |
    When A sell limit order is placed for 150 shares at 200
    Then The sell side of the order book should look like this:
      | side | quantity | limitPrice |
      | sell | 100      | 200        |
      | sell | 150      | 200        |
      | sell | 150      | 250        |
