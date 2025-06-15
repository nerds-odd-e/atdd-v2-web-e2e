# language: zh-CN
@logistics-api
功能: 查询快递

  场景: 查询快递
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
    那么"/express/query?number=4313751158896" should response:
      """
      : {
        code: 200
        body.json= {
          "status": 0,
          "msg": "ok",
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
      }
      """

  场景: 查询快递 - 404
    那么"/express/query?number=not-exist" should response:
      """
      code: 404
      """
