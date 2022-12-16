import { reactive } from "vue"
import axios from "axios"
import constants from "../constants"

export const AppVue = reactive({
    selectedText: '',
    pageSelected:'home'
})


//��������ѡ�У����䴢����selectedText�С�����ʵ�ֳ�����
export const init = ()=>{
    document.addEventListener("selectionchange", () => AppVue.selectedText = window.getSelection().toString())
    document.addEventListener("keyup", async (e)=>{
    if(e.key == '?' && e.ctrlKey) {
        await axios({
            method: "POST",
            url: constants.backHost + "utils/openLink",
            data: AppVue.selectedText,
            headers: {
                'Content-Type': 'text/plain'
            }
        }).then(() => {
            console.log("open link success.")
        })
    }
    })
}
