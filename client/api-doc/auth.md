## admin 账号

email: admin
password: 123456

## 获取所有角色列表

GET /admin/roles

## 获取所有用户

GET /admin/users

## 获取登录用户的权限

GET /admin/me

## 添加用户

POST /admin/users
body:

```json
{
  "email": "xxx",
  "password": "xxx",
  "status": "ENABLED",
  "roles": [
    {
      "id": "xxx",
      "name": "xxx",
      "status": "xxx",
      "description": "xxx"
    }
  ]
}
```

### 备注

1. roles 的数据从/admin/roles 接口中获取到
2. 添加用户的 status 字段可以写死为: ENABLED

## 获取分享列表

GET /share-material

## 保存（分享）任务

POST /share-material
body:

```json
{
  "type": "HTTP", // 可能的🈯值SQL,HTTP,DATA,REDIS,MQ,MONGO,CASE
  "materialId": "xxxx", // 任务id，（http，sql，case这些的id）
  "sharedUserIds": null // 如果想分享给所有人就是null，如果分享给特定的用户，需要userIds.join(',')
}
```

## 保存分享资源用户自定义的参数

POST /share-material/params

## 修改分享资源用户自定义的参数

PUT /share-material/params

## 删除分享资源用户自定义的参数

DELETE /share-material/params

### 上面三个接口都需要传 body

```json
{
  "id": "xxx",
  "shareMaterialId": "xxx",
  "params": "xxxx"
}
```
