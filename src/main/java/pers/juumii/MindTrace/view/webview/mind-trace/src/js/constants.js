const arrayRemove = (array, element)=>{
    array.splice(array.indexOf(element),1)
}
const durationTransfer = (totalSecond)=>{
    let second, minute, hour
    let totalMinute = Math.floor(totalSecond / 60)
    hour = Math.floor(totalMinute / 60)
    minute = totalMinute - 60 * hour
    second = totalSecond - 60 * totalMinute
    return [hour, minute, second]
}

export default{
    backHost:"http://localhost:9090/",
    arrayRemove,
    durationTransfer
}