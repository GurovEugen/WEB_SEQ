let std1 = {
   id: 1,
   name: 'Иванов'
  };

let std2 = {
   id: 2,
   name: 'Петров'
  };
  
let std3 = {
   id: 3,
   name: 'Сидоров'
  };
  
let students = [std1,std2,std3];

let token = '';
let refreshToken = '';

function test() {             
  fetch('/maven_web_test/api/test/access',{method: 'POST', headers: {AUTHORIZATION: 'Bearer '+ token,'Content-Type': 'application/json;charset=utf-8'},body: JSON.stringify(students)})
  .then(function(response) {        
      if (response.ok) {
        return response.json();
      }
      else {
	console.log(response);
	throw response.text();
      }	  	  
   })
  .then(function(data) {
      console.log(data)
	students = data;  
	let d = document.getElementById('divTest');      
        let t = d.firstChild;              
        t.nodeValue = JSON.stringify(students);
  })
  .catch(function(error) {
	console.log(error);
	alert("Error catch: " + error);  
  })
  ;
}

function refresh() {                
  fetch('/maven_web_test/api/test/refresh',{method: 'POST', headers: {AUTHORIZATION: 'Bearer '+ refreshToken,'Content-Type': 'application/json;charset=utf-8'},body: JSON.stringify(students)})
  .then(function(response) {        
      if (response.ok) {
        return response.text();
      }
      else {
        console.log(response);
	throw response.text();
      }	  	  
   })
  .then(function(data) {
      token = data.split(" ")[0];
      refreshToken = data.split(" ")[1];
      let d = document.getElementById('divTest1');      
      let t = d.firstChild;
      t.nodeValue = token + "\n" +refreshToken;
      
  })
  .catch(function(error) {
	console.log(error);
	alert("Error catch: " + error);  
  })
  ;
}
function login() {                
  let user = {   
   name: 'admin',
   password: '123456'
  };  
  fetch('/maven_web_test/api/login',{method: 'POST', headers: {'Content-Type': 'application/json;charset=utf-8'},body: JSON.stringify(user)})
  .then(function(response) {        
      if (response.ok) {
        return response.text();
      }
      else {
        console.log(response);
	throw response.text();
      }	  	  
   })
  .then(function(data) {
      token = data.split(" ")[0];
      refreshToken = data.split(" ")[1];
      let d = document.getElementById('divLogin');      
      let t = d.firstChild;
      t.nodeValue = token + "\n" +refreshToken;
      
  })
  .catch(function(error) {
	console.log(error);
	alert("Error catch: " + error);  
  })
  ;
}

function ping() {                            
  var xhr = new XMLHttpRequest();
    
  var flagAsync = false;
  xhr.open("GET", "/maven_web_test/api/ping", flagAsync);
    
  xhr.send();
  
  
  if (xhr.status !== 200) {  
    alert( "Request error: " + xhr.status + ': ' + xhr.statusText );
  } 
  else { 
    var response = xhr.responseText;   
    var d = document.getElementById("divPing");      
    var t = d.firstChild;            
    t.nodeValue = t.nodeValue + " " + response;
   } 
}