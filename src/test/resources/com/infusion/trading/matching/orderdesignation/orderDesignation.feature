Feature: Order designation

  Scenario: A fill or kill market order and there is not enough liquidity
   Given the order book looks like this before the trade is placed:
      | symbol | side | quantity | limitPrice |
      | FOO    | sell | 100      | 200        |
    When a Fill_or_Kill market buy order is placed for 300 shares of FOO
    Then the order should not be executed; the order book should look like this at the end of the trade:
      | symbol | side | quantity | limitPrice |
      | FOO    | sell | 100      | 200        |

  Scenario: A fill or kill market order and it is filled
    Given the order book looks like this before the trade is placed:
      | symbol	| side | quantity | limitPrice |
      | FOO 	| sell | 100      | 200        |
     When a Fill_or_Kill market buy order is placed for 100 shares of FOO
    Then the order should not be executed; the order book should look like this at the end of the trade:
      | symbol	| side	| quantity	| limitPrice	|
      | FOO 	|		|			|				|
