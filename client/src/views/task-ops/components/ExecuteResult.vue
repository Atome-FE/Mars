<template>
  <div :class="['record-ops', record.result ? 'record-ops-success' : 'record-ops-failed']"
    @click="gotoRecord"
    v-if="record.status === 'COMPLETED'">
    <div class="item">
      <span>
        {{ record.result ? '成功' : '失败' }}
      </span>
      <span>
        总执行数: {{ record.total }}
      </span>
      <span>
        总错误数: {{ record.errorTotal }}
      </span>
    </div>
    <div class="item">
      <span>
        http
      </span>
      <span>
        执行数: {{ record.httpTotal }}
      </span>
      <span>
        错误数: {{ record.httpErrorTotal }}
      </span>
    </div>
    <div class="item">
      <span>
        sql
      </span>
      <span>
        执行数: {{ record.sqlTotal }}
      </span>
      <span>
        错误数：{{ record.sqlErrorTotal }}
      </span>
    </div>
    <div class="item">
      <span>
        redis
      </span>
      <span>
        执行数: {{ record.redisTotal }}
      </span>
      <span>
        错误数：{{ record.redisErrorTotal }}
      </span>
    </div>
    <div class="item">
      <span>
        mq
      </span>
      <span>
        执行数: {{ record.mqTotal }}
      </span>
      <span>
        错误数：{{ record.mqErrorTotal }}
      </span>
    </div>
    <div class="item">
      <span>
        mongo
      </span>
      <span>
        执行数: {{ record.mongoTotal }}
      </span>
      <span>
        错误数：{{ record.mongoErrorTotal }}
      </span>
    </div>
  </div>
</template>

<script lang='tsx'>
import { Component, Vue, Prop } from 'vue-property-decorator'

import { RouteConstants } from '@/constants/route-constants'

@Component
export default class ExecuteResult extends Vue {
  @Prop(Object)
  record!: any

  @Prop(Number)
  executeTime!: number

  gotoRecord() {
    this.$router.push({
      name: RouteConstants.TASKRECORDHOME,
      query: {
        executeTime: this.executeTime.toString(),
      },
    })
  }
}
</script>

<style lang='stylus' scoped>
.record-ops-failed
  background-color #c00
  color white
.record-ops-success
  background-color #67C23A
  color black
.record-ops
  position relative
  display flex
  align-items center
  justify-content flex-start
  flex-wrap wrap
  margin-bottom 10px
  font-size 16px
  border-radius 5px
  .item
    position relative
    display flex
    align-items center
    margin 10px 40px 10px 10px
    span
      margin-right 10px
</style>
