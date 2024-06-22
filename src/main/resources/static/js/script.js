console.log("this is script file");

/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */

const toggleSidebar = () =>{
  if($(".sidebar").is(":visible")){
    $(".sidebar").css("display","none");
    $(".content").css("margin-left","0%");
  }else{
    $(".sidebar").css("display","block");
    $(".content").css("margin-left","20%");
  }
};

$('.input-group.date').datepicker({
        autoclose: true,
        todayHighlight: true
});

const search=()=>{
  let query=$("#search-input").val();
  if(query==''){
    $(".search-result").hide();

  }else{
    //search
    console.log(query);
    //sending request to server
    let url=`http://localhost:8282/search/${query}`;
    fetch(url)
     .then((response)=>{
       return response.json();
     })
     .then((data)=>{
       console.log(data);
       let text=`<div class='list-group'>`;
       data.forEach((policy)=>{
         text+=`<a href='/user/${policy.pId}/policy' class='list-group-item list-group-action'>${policy.name}</a>`
       });
       text+=`<\div>`;
       $(".search-result").html(text);
       $(".search-result").show();
     });
  }
;}