console.log("This is coming from js script!!!");

document.addEventListener("DOMContentLoaded", function () {
    // Search functionality
 
	const searchWrapper = document.getElementById("searchWrapper");
	const searchToggleBtn = document.getElementById("searchToggleBtn");
	const searchInput = document.getElementById("search-input");
	const searchResultsDropdown = document.getElementById("searchResultsDropdown");
	   
	let isSearchOpen = false;

	searchToggleBtn.addEventListener("click", () => {
	    isSearchOpen = !isSearchOpen;
	    searchWrapper.classList.toggle("active", isSearchOpen);
	    if (isSearchOpen) {
	        searchInput.focus(); 
	    } else {
	        searchResultsDropdown.classList.remove("active"); 
	    }
	});


	 searchInput.addEventListener("focus", () => {
	     searchResultsDropdown.classList.add("active");
	 });


	 document.addEventListener("click", (event) => {
	     if (!searchWrapper.contains(event.target) && !searchToggleBtn.contains(event.target)) {
	         searchResultsDropdown.classList.remove("active");
	     }
	 });
	

    // Sidebar functionality
    const menuToggle = document.getElementById('menu-toggle');
    const sidebarLogo = document.getElementById('sidebar-logo'); // The address book icon in the sidebar
    const body = document.body;

    function toggleSidebar() {
        body.classList.toggle('sidebar-open');
    }

    // Add event listeners to the menu toggle button (in navbar) and the sidebar logo
    if (menuToggle) {
        menuToggle.addEventListener('click', toggleSidebar);
    }
    if (sidebarLogo) {
        sidebarLogo.addEventListener('click', toggleSidebar);
    }
});


window.addEventListener("DOMContentLoaded", () => {

	const deletedMsg = document.getElementById("deletedMsg")?.value?.trim();
	const deleteType = document.getElementById("deletedtoastType")?.value || 'info';

	if (deletedMsg && deletedMsg.length > 0) {
	    toastr.options = {
	        "closeButton": true,
	        "progressBar": true,
	        "positionClass": "toast-top-center",
	        "timeOut": "5000",
	        "preventDuplicates": true,
	        "newestOnTop": true
	    };
	    toastr[deleteType](deletedMsg);
	}
	
	
	const updateMsg = document.getElementById("updateMsg")?.value?.trim();
	const updatedType = document.getElementById("updatedtoastType")?.value || 'info';

	if (updateMsg && updateMsg.length > 0) {
	    toastr.options = {
	        "closeButton": true,
	        "progressBar": true,
	        "positionClass": "toast-top-center",
	        "timeOut": "5000",
	        "preventDuplicates": true,
	        "newestOnTop": true
	    };
	    toastr[updatedType](updateMsg);
	}

});



const search=() => {
	
	let query = $("#search-input").val();
	
	
	if(query != ''){
	console.log(query);
	$(".search-results-dropdown").show();

	let url = `http://localhost:8080/search/${query}`;

	fetch(url).then((response) => {
		return response.json();
	}).then((data) =>{
		
		let text = `<div class='list-group'>`;

		data.forEach((contact) => {
			text += `<a href='/user/contactProfile/${contact.cid}' class='list-group-item list-group-action'>${contact.cname} </a>`
		});

		text+=`</div>`;


		$(".search-results-dropdown").html(text);


	});

	}else{
		$(".search-results-dropdown").hide();
	}

}




