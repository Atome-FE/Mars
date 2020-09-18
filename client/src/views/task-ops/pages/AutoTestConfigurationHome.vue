<template>
    <div width="100%">
        <br/>
        <span style="margin-bottom: 20px;" id="addBusinessLine">
            <el-button size="medium" @click="addBusinessLine">添加业务线</el-button>
            <el-button size="medium" @click="modifyBusinessLineName">修改业务线名称</el-button>
            <el-button size="medium" @click="manualRunCurrentBusinessLineTask">当前业务线手动执行</el-button>
            <el-button size="medium" @click="manualRunTask">全业务线手动执行</el-button>
            <el-button size="medium" @click="configSleepTime">修改用例执行间隔时间，当前值为{{sleepValue}}</el-button>
        </span>
        <br/><br/>
        <el-row :gutter="20">
            <el-col :span="12">
                <div>
                <el-tabs style="width: 500px">
                    <el-tab-pane>
                        <span slot="label" :id="generateBusinessLine(businessLine)" :class="isBusinessLineSelected()" v-for="(businessLine,index) in businessLineList" :key="businessLine" v-on:click="reload(businessLine)"><i class="el-icon-date"></i>{{businessLine}}&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <el-form>
                                <el-form-item label="脚本执行所在服务器访问方式:" label-width="200px">
                                    <el-input  style= "border:5px; border-color:white; width:300px; text-align:left; color:white" v-model="sshVisit" placeholder="例如 ssh sysop@172.21.32.139"></el-input>
                                </el-form-item>
                                <el-form-item label="脚本执行绝对路径:" label-width="200px">
                                    <el-input  style= "border:5px; border-color:white; width:300px; text-align:left; color:white" v-model="absoluteDir" placeholder="请输入自动化测试脚本所在位置"></el-input>
                                </el-form-item>
                                <el-form-item label="脚本执行主命令:" label-width="200px">
                                    <el-input  style= "border:5px; border-color:white; width:300px; text-align:left; color:white" v-model="mainCommend" placeholder="请输入自动化测试脚本主命令"></el-input>
                                </el-form-item>
                                <el-form-item label="脚本执行具体文件相对路径:" label-width="200px">
                                    <el-input  style= "border:5px; border-color:white; width:300px; text-align:left; color:white" v-model="relativeDir" placeholder="请输入自动化测试脚本执行具体文件相对路径"></el-input>
                                </el-form-item>
                                <el-form-item label="脚本执行参数:" label-width="200px">
                                    <el-input  style= "border:5px; border-color:white; width:300px; text-align:left; color:white" v-model="parameter" placeholder="请输入自动化测试脚本执行参数"></el-input>
                                </el-form-item>
                            </el-form>
                    </el-tab-pane>
                </el-tabs>
                <br/><br/><br/><br/>
                <span style="margin-bottom: 20px; width: 160px" id="addBusinessLine">
                    <el-button size="medium" @click="addNewTestCase">添加新执行用例
                    </el-button>
                </span>
                <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <span style="margin-bottom: 20px; width: 160px" id="modifyBusinessLine">
                    <el-button size="medium" @click="editNewTestCase">修改该用例
                    </el-button>
                </span>
                <span>&nbsp;&nbsp;&nbsp;&nbsp;</span>
                <span style="margin-bottom: 20px; width: 160px" id="deleteBusinessLine">
                    <el-button size="medium" @click="deleteNewTestCase">删除该用例
                    </el-button>
                </span>
                </div>
            </el-col>
            <el-col :span="5" offset="3">
                <div style="width: 500px">
                    <br>
                    <div style="text-align:left; color:#409eff; font-size:16px; margin-bottom: 10px">{{currentBusinessLine}}已设置完成的用例</div>
                    <div v-for="(autoTestConfiguration,index) in autoTestConfigurationList" :key="autoTestConfiguration" :id="generateId(index)" v-on:click="recordSelected(index)" :class="[getColor(index), 'basicStyle']">
                        {{autoTestConfiguration.mainCommend + " " + autoTestConfiguration.relativeDir + " " + autoTestConfiguration.parameter}}
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import { IAutomatedConfiguration } from '@/interfaces/test-case-configuration'
import { addNewTestCase, updateTestCase, deleteTestCase, getTestCases, getAllBusinessLine, updateBusinessLineName, manualRunTasks, manualRunCurrentBusinessLineTask, getSleepValue, setSleepValue } from '@/apis/automated-test-case-configuration'
import { Route } from 'vue-router'
import { delay } from 'lodash'
// import { getCSSClassStyles } from '../../../lib/utils/css-handler'

@Component
export default class AutoTestConfiguration extends Vue {
    currentBusinessLine = ''

    currentRecordId=''

    businessLineList:string[] = []

    mainCommend = '';

    absoluteDir = '';

    sshVisit = '';

    relativeDir = '';

    parameter = '';

    sleepValue = '';

    async configSleepTime() {
      const newSleepTime = prompt('更新后的等待时间为', '')
      if (newSleepTime) {
        await setSleepValue(newSleepTime)
      }
      this.getSleepValue()
    }

    async getAllBusinessLineMethod() {
      console.log('get all business line')
      const { data } = await getAllBusinessLine()
      data.forEach((element:string) => {
        if (this.businessLineList.indexOf(element) == -1) {
          this.businessLineList.push(element)
        }
      })
    }

    async modifyBusinessLineName() {
      const newBusinessLineName = prompt('业务线修改后名称', '')
      if (newBusinessLineName) {
        const { data } = await updateBusinessLineName({ 'newName': newBusinessLineName, 'oldName': this.currentBusinessLine })
        console.log(`update results ${data}`)
        const index = this.businessLineList.indexOf(this.currentBusinessLine)
        this.businessLineList.splice(index, 1)
        this.businessLineList.push(newBusinessLineName)
        this.currentBusinessLine = newBusinessLineName
        this.reload(this.currentBusinessLine)
      }
    }

    addBusinessLine() {
      console.log('addBusinessLine')
      const newBusinessLine = prompt('业务线名称', '')
      if (newBusinessLine) {
        this.businessLineList.push(newBusinessLine)
        this.reload(newBusinessLine)
      }
      console.log(this.businessLineList.toString())
    }

    isBusinessLineSelected() {
      this.businessLineList.forEach((element) => {
        console.log(this.currentBusinessLine)
        if (element == this.currentBusinessLine) {
          const item = document.getElementById(element)
          if (item) {
            item.setAttribute('class', 'colorStyleSelected')
          }
        } else {
          const item = document.getElementById(element)
          if (item) {
            item.setAttribute('class', '')
          }
        }
      })
    }

    generateBusinessLine(businessLine:string) {
      return businessLine
    }

    async manualRunTask() {
      const { status } = await manualRunTasks()
      console.log(status)
    }

    async manualRunCurrentBusinessLineTask() {
      const { status } = await manualRunCurrentBusinessLineTask(this.currentBusinessLine)
      console.log(status)
    }

    @Watch('mainCommend')
    onMainCommendChanged(mainCommend:string) {
      this.mainCommend = mainCommend
    }
    

    @Watch('absoluteDir')
    onAbsoluteDirChanged(absoluteDir:string) {
      this.absoluteDir = absoluteDir
    }

    
    @Watch('sshVisit')
    onSshVisit(value:string) {
      this.sshVisit = value
    }

    
    @Watch('relativeDir')
    onRelativeDirChanged(value:string) {
      this.relativeDir = value
    }

    
    @Watch('parameter')
    onParameterChanged(value:string) {
      this.parameter = value
    }
   

    addNewTestCase() {
      console.log('add new testCase')
      const newTestCase: IAutomatedConfiguration = {
        id: '',
        businessLine: this.currentBusinessLine,
        sshVisit: this.sshVisit,
        absoluteDir: this.absoluteDir,
        mainCommend: this.mainCommend,
        relativeDir: this.relativeDir,
        parameter: this.parameter,
      }
      this.addAutoTestConfiguration(newTestCase)
    }

    editNewTestCase() {
      console.log('edit')
      const testCase: IAutomatedConfiguration = {
        id: this.currentRecordId,
        businessLine: this.currentBusinessLine,
        sshVisit: this.sshVisit,
        absoluteDir: this.absoluteDir,
        mainCommend: this.mainCommend,
        relativeDir: this.relativeDir,
        parameter: this.parameter,
      }
      this.editAutoTestConfiguration(testCase)
    }

    deleteNewTestCase() {
      console.log('delete')
      const testCase: IAutomatedConfiguration = {
        id: this.currentRecordId,
        businessLine: this.currentBusinessLine,
        sshVisit: this.sshVisit,
        absoluteDir: this.absoluteDir,
        mainCommend: this.mainCommend,
        relativeDir: this.relativeDir,
        parameter: this.parameter,
      }
      this.deleteAutoTestConfiguration(testCase)
    }

    generate_color_list() {
      const colorList:string[] = []
      for (let i:number = 0; i < 20; i++) {
        colorList.push('colorStyleUnselected')
      }
      console.log(`generate_color_list: ${colorList.toString()}`)
      return colorList
    }

    colorList:string[] = this.generate_color_list()

    getColor(id:string) {
      console.log('get color')
      return this.colorList[parseInt(id)]
    }

    recordSelected(id:string) {
      console.log(`color change id ${id.toString()} ${this.colorList[parseInt(id)]}`)
      console.log('update')
      let index = 0
      let eachItem
      this.autoTestConfigurationList.forEach((element) => {
        eachItem = document.getElementById(index.toString())
        if (index != parseInt(id) && eachItem && eachItem.getAttribute('class') == 'colorStyleSelected basicStyle') eachItem.setAttribute('class', 'colorStyleUnselected basicStyle')
        // this.colorList[index] = "colorStyleUnselected"
        // console.log("index: " + index.toString() + " " + this.colorList[index])
        index += 1
      })

      const item = document.getElementById(id)
      if (item && item.getAttribute('class') == 'colorStyleUnselected basicStyle') {
        console.log(item.getAttribute('class'))
        item.setAttribute('class', 'colorStyleSelected basicStyle')
        // this.colorList[parseInt(id)] = "colorStyleSelected"
        console.log(item.getAttribute('class'))
        this.mainCommend = this.autoTestConfigurationList[parseInt(id)].mainCommend
        this.absoluteDir = this.autoTestConfigurationList[parseInt(id)].absoluteDir
        this.relativeDir = this.autoTestConfigurationList[parseInt(id)].relativeDir
        this.parameter = this.autoTestConfigurationList[parseInt(id)].parameter
        this.sshVisit = this.autoTestConfigurationList[parseInt(id)].sshVisit
        this.currentBusinessLine = this.autoTestConfigurationList[parseInt(id)].businessLine
        this.currentRecordId = this.autoTestConfigurationList[parseInt(id)].id
      } else if (item) {
        console.log(item.getAttribute('class'))
        item.setAttribute('class', 'colorStyleUnselected basicStyle')
        // this.colorList[parseInt(id)] = "colorStyleUnselected"
        console.log(item.getAttribute('class'))
        // this.mainCommend = ''
        // this.absoluteDir = ''
        this.relativeDir = ''
        this.parameter = ''
        // this.sshVisit = ''
        this.currentRecordId = ''
      }
    }
    
    generateId(index:number) {
      console.log(`index: ${index.toString()}`)
      return index.toString()
    }

    autoTestConfigurationList:IAutomatedConfiguration[] = []

    load(data:IAutomatedConfiguration[]) {
      this.autoTestConfigurationList = []
      data.forEach(element => {
        this.autoTestConfigurationList.push(element)
      })
      this.getAllBusinessLineMethod()
      console.log( `this.businessLineList${this.businessLineList.toString()}`)
    }
    
    async addAutoTestConfiguration(newTestCase: IAutomatedConfiguration) {
      const { status } = await addNewTestCase(newTestCase)
      console.log(status)
      this.autoTestConfigurationList = []
      this.getAutoTestConfigurationList()
    }

    async editAutoTestConfiguration(testCase: IAutomatedConfiguration) {
      const { status } = await updateTestCase(testCase)
      this.autoTestConfigurationList = []
      this.getAutoTestConfigurationList()
    }

    async deleteAutoTestConfiguration(testCase: IAutomatedConfiguration) {
      const { status } = await deleteTestCase(testCase)
      this.autoTestConfigurationList = []
      this.getAutoTestConfigurationList()
    }
    
    async getAutoTestConfigurationList() {
      console.log(`getAutoTestConfigurationList: ${this}`)
      const { data } = await getTestCases(this.currentBusinessLine)
      console.log(data)
      this.load(data)
    }

    async getSleepValue() {
      const { data } = await getSleepValue()
      console.log(data)
      this.sleepValue = data.autoTestSleep
    }

    // beforeRouteEnter(to: Route, from: Route, next: any) {
    //     next((vm: AutoTestConfiguration) =>{
    //         vm.getAutoTestConfigurationList()
    //         console.log(vm.currentBusinessLine)
    //     })
    // }
    created() {
      this.getAutoTestConfigurationList()
      this.getSleepValue()
    }

    reload(businessLine:string) {
      this.currentBusinessLine = businessLine
      this.mainCommend = ''
      this.absoluteDir = ''
      this.relativeDir = ''
      this.parameter = ''
      this.sshVisit = ''
      this.currentRecordId = ''
      this.getAutoTestConfigurationList()
    }
}

</script>

<style>
.basicStyle{
    text-align:left;
    font-size:14px;
    margin-bottom: 10px;
}
.colorStyleUnselected{
    color: black;
}
.colorStyleSelected{
    color:red;
}
</style>
