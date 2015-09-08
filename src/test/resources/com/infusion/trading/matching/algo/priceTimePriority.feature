Feature: Price Time Priority

  Scenario: A sell limit order is added to the order book at a position according to price time priority
    Given The order book looks like this before the trade is placed:
      | symbol	| side | quantity | limitPrice |
      | FOO 	| sell | 100      | 200        |
      | FOO 	| sell | 150      | 250        |
    When A sell limit order is placed for 150 shares of FOO at 100
    Then The order book should look like this at the end of the trade:
      | symbol	| side | quantity | limitPrice |
      | FOO 	| sell | 150      | 100        |
      | FOO 	| sell | 100      | 200        |
      | FOO 	| sell | 150      | 250        |

  Scenario: A buy limit order is added to the buy side order book at a position according to price time priority
    Given The order book looks like this before the trade is placed:
      | symbol 	| side	| quantity | limitPrice |
      | FOO 	| buy		| 100      | 250        |
      | FOO 	| buy		| 150      | 200        |
    When A buy limit order is placed for 150 shares of FOO at 100
    Then The order book should look like this at the end of the trade:
      | symbol	| side	| quantity | limitPrice |
      | FOO 	| buy		| 100  | 250        |
      | FOO 	| buy		| 150  | 200        |
      | FOO 	| buy		| 150  | 100        |

  Scenario: A limit order is added to the order book while another order exists for the same price
    Given The order book looks like this before the trade is placed:
      | symbol	| side | quantity | limitPrice | 
      | FOO 	| sell | 100      | 200        |
      | FOO 	| sell | 150      | 250        |
    When A sell limit order is placed for 150 shares of FOO at 200
    Then The order book should look like this at the end of the trade:
      | symbol 	| side | quantity | limitPrice |
      | FOO 	| sell | 100      | 200        |
      | FOO 	| sell | 150      | 200        |
      | FOO 	| sell | 150      | 250        |
