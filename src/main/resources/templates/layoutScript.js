//            $.widget.bridge('uibutton', $.ui.button);


            // Assuming you have a reference to your button element
            var toggleButton = document.getElementById('toggleMarkerMode');

            // Toggle the 'active' class on button click
            toggleButton.addEventListener('click', function () {
                this.classList.toggle('active');


                // Update the button text based on the presence of the 'active' class
                if (this.classList.contains('active')) {
                    toggleButton.textContent = 'Cancel';
                } else {
                    toggleButton.textContent = 'Add aid';
                }

            });