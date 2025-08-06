console.log("This is coming from js script!!!");

document.addEventListener("DOMContentLoaded", function () {
    // Search functionality
    const searchWrapper = document.getElementById("searchWrapper");
    const searchToggleBtn = document.getElementById("searchToggleBtn");
    const navLinks = document.querySelector(".nav-links");

    let isSearchOpen = false;

    searchToggleBtn.addEventListener("click", () => {
        isSearchOpen = !isSearchOpen;
        searchWrapper.classList.toggle("active", isSearchOpen);
        navLinks.classList.toggle("shifted", isSearchOpen);
    });

    // Sidebar functionality
    const menuToggle = document.getElementById('menu-toggle');
    const sidebarLogo = document.getElementById('sidebar-logo'); // The address book icon in the sidebar
    const body = document.body;

    /**
     * Toggles the sidebar open/closed state.
     * Adds or removes the 'sidebar-open' class from the body,
     * which triggers CSS transitions for sidebar movement and main content resizing.
     */
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

// Toastr messages (for captcha and logout)
window.addEventListener("DOMContentLoaded", () => {
    const msg = document.getElementById("captchaResponse")?.value?.trim();
    const type = document.getElementById("toastType")?.value || 'info';

    if (msg && msg.length > 0) {
        toastr.options = {
            "closeButton": true,
            "progressBar": true,
            "positionClass": "toast-top-center",
            "timeOut": "4000",
            "preventDuplicates": true,
            "newestOnTop": true
        };
        toastr[type](msg);
    }

    const logout = document.getElementById("logoutPopupMessage")?.value?.trim();
    const logoutType = document.getElementById("logouttoastType")?.value || 'info';

    if (logout && logout.length > 0) {
        toastr.options = {
            "closeButton": true,
            "progressBar": true,
            "positionClass": "toast-top-center",
            "timeOut": "5000",
            "preventDuplicates": true,
            "newestOnTop": true
        };
        toastr[logoutType](logout);
    }
	
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


