const base = {
    get() {
        return {
            url : "http://localhost:8080/wangshangdingcan/",
            name: "wangshangdingcan",
            // 退出到首页链接
            indexUrl: 'http://localhost:8080/wangshangdingcan/front/index.html'
        };
    },
    getProjectName(){
        return {
            projectName: "网上订餐系统"
        } 
    }
}
export default base
