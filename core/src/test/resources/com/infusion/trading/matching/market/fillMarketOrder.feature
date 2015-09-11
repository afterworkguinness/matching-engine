Feature: Market order

  Scenario: A market buy order is placed
   Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | sell | 700      | 100        |
    When A market buy order is placed for 600 shares of FOO
    Then The order book should look like this at the end of the trade:
      | symbol | side | quantity | limitPrice |
      | FOO    | sell | 100      | 100        |
      

   Scenario: A market buy order is placed and does not touch the resting limit order for another stock
    Given The order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | BAR    | sell | 500      | 500        |
      | FOO    | sell | 700      | 100        |
    When A market buy order is placed for 600 shares of FOO
    Then The order book should look like this at the end of the trade:
      | symbol | side | quantity | limitPrice |
      | BAR    | sell | 500      | 500        |
      | FOO    | sell | 100      | 100        |

  Scenario: A market buy order is placed and matched by multiple limit orders
    Given The order book looks like this before the trade is placed:
      | symbol	| side | quantity | limitPrice |
      | FOO		| sell | 500      | 100        |
      | FOO		| sell | 400      | 100        |
    When A market buy order is placed for 600 shares of FOO
    Then The order book should look like this at the end of the trade:
      | symbol	| side | quantity | limitPrice |
      | FOO		| sell | 300      | 100        |

  Scenario: A market buy order is converted to a limit order when liquidity runs out
    Given The order book looks like this before the trade is placed:
      | symbol	| side | quantity | limitPrice |
      | FOO		| sell | 500      | 100        |
      | FOO		| sell | 100      | 200        |
    When A market buy order is placed for 700 shares of FOO
    Then The order book should look like this at the end of the trade:
      | symbol	| side | quantity | limitPrice |
      | FOO		| buy  | 100      | 200        |

  #Scenario: A market buy order is placed against an empty book. It is converted to a limit order and added to the book
  #TODO: need to get closing price is no last traded price then check rqmts doc