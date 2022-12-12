import { reactive } from "vue";

export default reactive({
    kTreeList: [],
    selectedKTree: '',
    kRoot: {},
    selectedText: '',
    settings:{},
    kTreeConfigs:{},
    AppVue:{
        // 'home' / 'repository'
        pageSelected:'home'
    },
    Repository:{
        selectedIndexes: [],
        toBeShifted: {},
        toBePasted: {},
        quizCardToBeShifted: undefined
    },
    Home:{
        QuizTask:{
            quizIsDue:false,
            quizCards:[],
            finished:[]
        },
        LearningTask:{
            startTime: undefined,
            description:'',
            kTreeOptions:[]
        },
        MindTrace:{
            overviewMarkdown: '',
            quizCardTrace:[]
        },
        // 'QuizTask' / 'LearningTask' / 'MindTrace'
        pageSelected:'LearningTask'
    }
})