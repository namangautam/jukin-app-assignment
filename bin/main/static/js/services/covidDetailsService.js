covidApp.service('covidDetailsService',function($http){
	
		this.fetchCountries=function(){
			var cntryData =  $http.get('/countries').then(success,error);
			return cntryData;
		}
		
		
		this.fetchCountryCodeMap=function(){
			var cntryMapData =  $http.get('/countriesMap').then(success,error);
			return cntryMapData;
		}
		this.loadCountryDataByCode = function(code){
			return $http.get('/covidDetailsByCode/'+code).then(success,error);
		}
		
		this.getTotal  = function(){
			var totalData =  $http.get('/covidDetails').then(success,error);
			return totalData;
		}
		
		this.loadCountryDataByName=function(name){
			return $http.get('/covidDetailsByName/'+name).then(success,error);
		}
		
		this.loadCountryDataByCode=function(code){
			return $http.get('/covidDetailsByCode/'+code).then(success,error);
		}
		
		this.updateCountry = function(country){
			var body = {
				"country": country
			} 
			return $http.post('/updateCountry',country) .then(success,error);
		}
		this.addCommentByCode=function(code,comment){
			var body = {
				"code": code,
				"comment": comment
			} 
			return $http.post('/addCommentsByCode',body) .then(success,error);
		}
		
		this.addCommentByName=function(name,comment){
			var body = {
				"name": name,
				"comment": comment
			} 
			return $http.post('/addCommentsByName',body) .then(success,error);
		}
		
		this.retrieveCommentByCode=function(value){
			return $http.get('/commentByCode/'+value).then(success,error);
		}
		this.retrieveCommentByName = function(value){
			return $http.get('/commentByName/'+value).then(success,error);
		}
		

		
		function success(data,error){
			console.log("data"+data);
			return data.data;
		}
		
		function error(err){
			console.log("error"+err);
			return err.data;
		}
})