# language: zh-CN
@api-login
功能: 订单-jfactory

  场景: 订单列表
    假如存在"订单":
    """
    {
      "code": "SN001",
      "productName": "电脑",
      "total": "19999",
      "status": "toBeDelivered"
    }
    """
    当GET "/orders"
    那么response should be:
    """
    : {
      code: 200
      body.json= [{
        code= SN001
        productName= 电脑
        total: 19999.0
        status= toBeDelivered
      }]
    }
    """

  场景: 订单详情 - 订单项
    假如存在"订单":
    """
    {
      "code": "SN001",
      "productName": "电脑",
      "total": "19999",
      "status": "toBeDelivered",
      "deliverNo": null,
      "lines": [{
        "itemName": "电脑",
        "price": "19999",
        "quantity": 1
      }]
    }
    """
    那么"订单.code[SN001]"应为:
    """
    : {
        code= SN001
        productName= 电脑
        total: 19999
        status: toBeDelivered
        lines: [{
          itemName= 电脑
          price: 19999
          quantity= 1
        }]
    }
    """

  场景: 订单详情 - 查询物流
    假如存在"已发货的 订单":
      | code  | deliverNo     |
      | SN001 | 4313751158896 |
    并且存在接口数据"物流信息响应"并匹配查询参数"number=4313751158896":
    """
    {
        "result": {
            "number": "4313751158896",
            "type": "yunda",
            "typename": "韵达快运",
            "logo": "https://api.jisuapi.com/express/static/images/logo/80/yunda.png",
            "list": [
                {
                    "time": "2021-04-16 23:51:55",
                    "status": "【潍坊市】已离开 山东潍坊分拨中心；发往 成都东地区包"
                },
                {
                    "time": "2021-04-16 23:45:47",
                    "status": "【潍坊市】已到达 山东潍坊分拨中心"
                },
                {
                    "time": "2021-04-16 16:47:35",
                    "status": "【潍坊市】山东青州市公司-赵良涛(13606367012) 已揽收"
                }
            ],
            "deliverystatus": 1,
            "issign": 0
        }
    }
    """
    当GET "/orders/SN001"
    那么response should be:
    """
    body.json.logistics: {
      deliverNo= '4313751158896'
      companyCode= yunda
      companyName= 韵达快运
      companyLogo= 'https://api.jisuapi.com/express/static/images/logo/80/yunda.png'
      details= | time                | status                                               |
               | 2021-04-16 23:51:55 |【潍坊市】已离开 山东潍坊分拨中心；发往 成都东地区包  |
               | 2021-04-16 23:45:47 |【潍坊市】已到达 山东潍坊分拨中心                     |
               | 2021-04-16 16:47:35 |【潍坊市】山东青州市公司-赵良涛(13606367012) 已揽收   |
      deliveryStatus= 在途中
      isSigned= 未签收
    }
    """

  场景: 订单发货
    假如存在"未发货的 订单":
      | code  |
      | SN001 |
    并且当前时间为"2000-05-10T20:00:00Z"
    当通过API发货订单"SN001"，快递单号为"SF001"
    那么"订单.code[SN001]"应为:
    """
    : {
        deliveredAt: '2000-05-10T20:00:00Z'
        deliverNo: 'SF001'
        status: 'delivering'
      }
    """

  场景: 订单自动完成
    假如存在"十五天前 已发货的 订单":
      | code     |
      | 状态和时间都符合 |
    假如存在"未发货的 订单":
      | code      |
      | 状态不符的时间符合 |
    假如存在"未到十五天 已发货的 订单":
      | code      |
      | 状态符合时间不符合 |
    当订单任务运行时
    那么"订单.code[状态和时间都符合]"最终应为:
    """
      status: 'done'
    """
    那么"订单.code[状态不符的时间符合]"应为:
    """
      status: 'toBeDelivered'
    """
    那么"订单.code[状态符合时间不符合]"应为:
    """
      status: 'delivering'
    """
