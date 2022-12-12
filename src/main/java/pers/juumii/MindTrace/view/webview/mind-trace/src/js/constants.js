const arrayRemove = (array, element)=>{
    array.splice(array.indexOf(element),1)
}
const getLast = (array)=>{
    return array[array.length-1]
}
const durationTransfer = (totalSecond)=>{
    let second, minute, hour
    let totalMinute = Math.floor(totalSecond / 60)
    hour = Math.floor(totalMinute / 60)
    minute = totalMinute - 60 * hour
    second = totalSecond - 60 * totalMinute
    return [hour, minute, second]
}
const durationTransferToString = (totalSecond)=>{
    return durationTransfer(totalSecond)[0] == 0 && durationTransfer(totalSecond)[1] == 0 ? '' : durationTransfer(totalSecond)[0] + ' : ' + durationTransfer(totalSecond)[1]
}
const now = ()=>{
    let date = new Date()
    date.setHours(date.getHours() + 8)
    return date.toISOString().split(".")[0]
}
const nowTime = ()=>{
    return now().split('T')[1]
}
const nowDate = ()=>{
    return now().split('T')[0]
}
const timestamp = (startTime, endTime)=>{
    return startTime.replace('T',' ') + " ~ " + endTime.split('T')[1]
}

export default{
    backHost:"http://localhost:9090/",
    arrayRemove,
    getLast,
    now,
    nowTime,
    nowDate,
    durationTransfer,
    durationTransferToString,
    timestamp
}