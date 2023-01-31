console.log("this is script file and i had attached it here");
// write hiding configureation for side bar
const toggleSidebar= () => {

if($('.sidebar').is(":visible"))
{
    $(".sidebar").css("display","none");
    $(".content").css("margin-left","0%");
}
else
{
$(".sidebar").css("display","block");
$(".content").css("margin-left","20%");

}

};

//STEP : 35
//writint the configuration for search Controller

const search=() =>{

    //console.log("searching...");

    let query=$("#search-input").val();         //attach the jquery if, not presnt
    console.log(query);

    if(query==''){
        $(".search-result").hide();
    }
   else{
    console.log(query);

    //sending request to server

    let url=`http://localhost:8282/search/${query}`;

    fetch(url)
      .then((response) => {
        return response.json();
    })
    .then((data) => {
   
     //   console.log(data);

let text=`<div class='list-group'>`;

     data.forEach((contact) => {
     text+=`<a href="/user/${contact.cid}/contact"  class="list-group-item list-group-action">${contact.name}</a>`
     });
        
text += `</div>`;

       $(".search-result").html(text);
       $(".search-result").show();
       

    });


    $(".search-result").show();
   }

};