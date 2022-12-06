import axios from 'axios'
import constants from './constants'

const getStatistics = async (methodName)=>{
    let res
    await axios.get(constants.backHost + "statistics/general?methodName=" + methodName).then(e => res = e.data)
    console.log("get statistics ["+methodName+"]", res)
    return res
}

export default{
    getStatistics
}