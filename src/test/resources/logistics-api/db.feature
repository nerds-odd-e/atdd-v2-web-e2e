# language: zh-CN
@api-login
功能: db relation

  场景: 未发货的-1
    假如存在"订单_物流api":
      | code  |
      | SN001 |
    那么DB should:
      """
      orders: | code  | deliver_no |
              | SN001 | null       |
      """
    并且"/orders/SN001" should response:
      """
      body.json.logistics= null
      """

  场景: 未发货的-2
    假如存在"未发货的 订单_物流api":
      | code  |
      | SN001 |
    那么DB should:
      """
      orders: | code  | deliver_no |
              | SN001 | null       |
      """
    并且"/orders/SN001" should response:
      """
      body.json.logistics= null
      """

  场景: 有单号无数据
    假如存在"订单_物流api":
      | code  | deliverNo |
      | SN001 | d1        |
    那么DB should:
      """
      orders: | code  | deliver_no |
              | SN001 | d1         |
      """
    并且"/orders/SN001" should response:
      """
      code= 500
      """

    并且"/express/query?number=d1" should response:
      """
      code: 404
      """

  场景: 指定单号的发货订单
    假如存在"已发货的 订单_物流api":
      | code  | express.number |
      | SN001 | d1             |
    那么DB should:
      """
      orders: | code  | deliver_no |
              | SN001 | d1         |
      """
    并且"/orders/SN001" should response:
      """
      body.json.logistics.deliverNo= d1
      """
