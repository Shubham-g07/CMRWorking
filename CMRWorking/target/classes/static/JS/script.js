console.log("This is coming from js script!!!");

document.addEventListener("DOMContentLoaded", function () {
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
	
	
	const unmatchedMsg = document.getElementById("unmatched")?.value?.trim();
	const unmatchedType = document.getElementById("unmatchedType")?.value || 'info';

	if (unmatchedMsg && unmatchedMsg.length > 0) {
	    toastr.options = {
	        "closeButton": true,
	        "progressBar": true,
	        "positionClass": "toast-top-center",
	        "timeOut": "5000",
	        "preventDuplicates": true,
	        "newestOnTop": true
	    };
	    toastr[unmatchedType](unmatchedMsg);
	}
	

});



document.addEventListener('DOMContentLoaded', () => {
    const toggleButtons = document.querySelectorAll('.password-toggle-button');
    toggleButtons.forEach(button => {
        button.addEventListener('click', () => {
            const targetId = button.dataset.target;
            const passwordInput = document.getElementById(targetId);
            const isPassword = passwordInput.type === 'password';
            
            // Toggle the input type
            passwordInput.type = isPassword ? 'text' : 'password';

            // Update the eye icon (simple change for now)
            const eyeIconPath = 'M10 12a2 2 0 100-4 2 2 0 000 4z';
            const eyeOffIconPath = 'M13.832 9.42a2 2 0 01-1.415-2.072A1.999 1.999 0 0110 6a2 2 0 012-2 1.999 1.999 0 012.417 1.347A.999.999 0 0114.6 6.5l.388.388a.999.999 0 010 1.414l-.999.999-.388.388a.999.999 0 01-1.414 0zM10 10a2 2 0 100 4 2 2 0 000-4z';
            
            const newIconPath = isPassword ? eyeOffIconPath : eyeIconPath;
            const svgPath = button.querySelector('path:first-child');
            svgPath.setAttribute('d', newIconPath);
        });
    });
});