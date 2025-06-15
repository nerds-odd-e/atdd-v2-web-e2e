# language: zh-CN
@api-login
功能: 订单-物流服务

  场景: 订单详情 - 查询物流 - 手动设置deliverNo(可以用于构造脏数据，有deliverNo而对端系统没有相应数据)
    假如存在"已发货的 订单_物流api":
      """
      | code  | deliverNo     | express |
      | SN001 | 4313751158896 | null    |
      """
    假如存在:
      """
      快递信息: {
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

  场景: 订单详情 - 查询物流 (同时创建订单和物流信息)
    假如存在"已发货的 订单_物流api":
    """
    code: SN001
    express: {
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

  场景: 订单详情 - 查询物流 (完全默认的物流信息)
    假如存在"已发货的 订单_物流api":
    """
    code: SN001
    """
    当GET "/orders/SN001"
    那么response should be:
    """
    body.json.logistics: {
      deliverNo= /^number.*/
    }
    """
