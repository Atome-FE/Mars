export enum ParamReplaceTaskEnum {
  HTTP = 'http',
  SQL = 'sql',
  MQ = 'mq',
  MONGO = 'mongo',
  REDIS = 'redis',
  DATA = 'data',
}
export enum ParamReplaceTypeEnum {
  BODY = 'body',
  PATH = 'path',
  QUERY = 'query',
  HEADERS = 'headers',
  KEY = 'key',
  EXCHANGE = 'exchange',
  ROUTINGKEY = 'routingKey',
  CONTENT = 'content',
  STATEMENT = 'statement',
  SOURCE = 'source',
  // TODO 下面两个枚举废弃了
  NORMAL = 'normal',
  SQL = 'sql',
}
