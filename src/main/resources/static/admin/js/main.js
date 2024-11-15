/**
* Template Name: NiceAdmin - v2.2.2
* Template URL: https://bootstrapmade.com/nice-admin-bootstrap-admin-html-template/
* Author: BootstrapMade.com
* License: https://bootstrapmade.com/license/
*/
(function() {
  "use strict";

  /**
   * Easy selector helper function
   */
  const select = (el, all = false) => {
    el = el.trim()
    if (all) {
      return [...document.querySelectorAll(el)]
    } else {
      return document.querySelector(el)
    }
  }

  /**
   * Easy event listener function
   */
  const on = (type, el, listener, all = false) => {
    if (all) {
      select(el, all).forEach(e => e.addEventListener(type, listener))
    } else {
      select(el, all).addEventListener(type, listener)
    }
  }

  /**
   * Easy on scroll event listener 
   */
  const onscroll = (el, listener) => {
    el.addEventListener('scroll', listener)
  }

  /**
   * Sidebar toggle
   */
  if (select('.toggle-sidebar-btn')) {
    on('click', '.toggle-sidebar-btn', function(e) {
      select('body').classList.toggle('toggle-sidebar')
    })
  }

  /**
   * Search bar toggle
   */
  if (select('.search-bar-toggle')) {
    on('click', '.search-bar-toggle', function(e) {
      select('.search-bar').classList.toggle('search-bar-show')
    })
  }

  /**
   * Navbar links active state on scroll
   */
  let navbarlinks = select('#navbar .scrollto', true)
  const navbarlinksActive = () => {
    let position = window.scrollY + 200
    navbarlinks.forEach(navbarlink => {
      if (!navbarlink.hash) return
      let section = select(navbarlink.hash)
      if (!section) return
      if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
        navbarlink.classList.add('active')
      } else {
        navbarlink.classList.remove('active')
      }
    })
  }
  window.addEventListener('load', navbarlinksActive)
  onscroll(document, navbarlinksActive)

  /**
   * Toggle .header-scrolled class to #header when page is scrolled
   */
  let selectHeader = select('#header')
  if (selectHeader) {
    const headerScrolled = () => {
      if (window.scrollY > 100) {
        selectHeader.classList.add('header-scrolled')
      } else {
        selectHeader.classList.remove('header-scrolled')
      }
    }
    window.addEventListener('load', headerScrolled)
    onscroll(document, headerScrolled)
  }

  /**
   * Back to top button
   */
  let backtotop = select('.back-to-top')
  if (backtotop) {
    const toggleBacktotop = () => {
      if (window.scrollY > 100) {
        backtotop.classList.add('active')
      } else {
        backtotop.classList.remove('active')
      }
    }
    window.addEventListener('load', toggleBacktotop)
    onscroll(document, toggleBacktotop)
  }

  /**
   * Initiate tooltips
   */
  var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
  var tooltipList = tooltipTriggerList.map(function(tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl)
  })

  /**
   * Initiate quill editors
   */
  if (select('.quill-editor-default')) {
    new Quill('.quill-editor-default', {
      theme: 'snow'
    });
  }

  if (select('.quill-editor-bubble')) {
    new Quill('.quill-editor-bubble', {
      theme: 'bubble'
    });
  }

  if (select('.quill-editor-full')) {
    new Quill(".quill-editor-full", {
      modules: {
        toolbar: [
          [{
            font: []
          }, {
            size: []
          }],
          ["bold", "italic", "underline", "strike"],
          [{
              color: []
            },
            {
              background: []
            }
          ],
          [{
              script: "super"
            },
            {
              script: "sub"
            }
          ],
          [{
              list: "ordered"
            },
            {
              list: "bullet"
            },
            {
              indent: "-1"
            },
            {
              indent: "+1"
            }
          ],
          ["direction", {
            align: []
          }],
          ["link", "image", "video"],
          ["clean"]
        ]
      },
      theme: "snow"
    });
  }

  /**
   * Initiate TinyMCE Editor
   */

  var useDarkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;

  tinymce.init({
    selector: 'textarea.tinymce-editor',
    plugins: 'print preview paste importcss searchreplace autolink autosave save directionality code visualblocks visualchars fullscreen image link media template codesample table charmap hr pagebreak nonbreaking anchor toc insertdatetime advlist lists wordcount imagetools textpattern noneditable help charmap quickbars emoticons',
    imagetools_cors_hosts: ['picsum.photos'],
    menubar: 'file edit view insert format tools table help',
    toolbar: 'undo redo | bold italic underline strikethrough | fontselect fontsizeselect formatselect | alignleft aligncenter alignright alignjustify | outdent indent |  numlist bullist | forecolor backcolor removeformat | pagebreak | charmap emoticons | fullscreen  preview save print | insertfile image media template link anchor codesample | ltr rtl',
    toolbar_sticky: true,
    autosave_ask_before_unload: true,
    autosave_interval: '30s',
    autosave_prefix: '{path}{query}-{id}-',
    autosave_restore_when_empty: false,
    autosave_retention: '2m',
    image_advtab: true,
    link_list: [{
        title: 'My page 1',
        value: 'https://www.tiny.cloud'
      },
      {
        title: 'My page 2',
        value: 'http://www.moxiecode.com'
      }
    ],
    image_list: [{
        title: 'My page 1',
        value: 'https://www.tiny.cloud'
      },
      {
        title: 'My page 2',
        value: 'http://www.moxiecode.com'
      }
    ],
    image_class_list: [{
        title: 'None',
        value: ''
      },
      {
        title: 'Some class',
        value: 'class-name'
      }
    ],
    importcss_append: true,
    file_picker_callback: function(callback, value, meta) {
      /* Provide file and text for the link dialog */
      if (meta.filetype === 'file') {
        callback('https://www.google.com/logos/google.jpg', {
          text: 'My text'
        });
      }

      /* Provide image and alt text for the image dialog */
      if (meta.filetype === 'image') {
        callback('https://www.google.com/logos/google.jpg', {
          alt: 'My alt text'
        });
      }

      /* Provide alternative source and posted for the media dialog */
      if (meta.filetype === 'media') {
        callback('movie.mp4', {
          source2: 'alt.ogg',
          poster: 'https://www.google.com/logos/google.jpg'
        });
      }
    },
    templates: [{
        title: 'New Table',
        description: 'creates a new table',
        content: '<div class="mceTmpl"><table width="98%%"  border="0" cellspacing="0" cellpadding="0"><tr><th scope="col"> </th><th scope="col"> </th></tr><tr><td> </td><td> </td></tr></table></div>'
      },
      {
        title: 'Starting my story',
        description: 'A cure for writers block',
        content: 'Once upon a time...'
      },
      {
        title: 'New list with dates',
        description: 'New List with dates',
        content: '<div class="mceTmpl"><span class="cdate">cdate</span><br /><span class="mdate">mdate</span><h2>My List</h2><ul><li></li><li></li></ul></div>'
      }
    ],
    template_cdate_format: '[Date Created (CDATE): %m/%d/%Y : %H:%M:%S]',
    template_mdate_format: '[Date Modified (MDATE): %m/%d/%Y : %H:%M:%S]',
    height: 600,
    image_caption: true,
    quickbars_selection_toolbar: 'bold italic | quicklink h2 h3 blockquote quickimage quicktable',
    noneditable_noneditable_class: 'mceNonEditable',
    toolbar_mode: 'sliding',
    contextmenu: 'link image imagetools table',
    skin: useDarkMode ? 'oxide-dark' : 'oxide',
    content_css: useDarkMode ? 'dark' : 'default',
    content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }'
  });

  /**
   * Initiate Bootstrap validation check
   */
  var needsValidation = document.querySelectorAll('.needs-validation')

  Array.prototype.slice.call(needsValidation)
    .forEach(function(form) {
      form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
          event.preventDefault()
          event.stopPropagation()
        }

        form.classList.add('was-validated')
      }, false)
    })

  /**
   * Initiate Datatables
   */
  const datatables = select('.datatable', true)
  datatables.forEach(datatable => {
    new simpleDatatables.DataTable(datatable);
  })

  /**
   * Autoresize echart charts
   */
  const mainContainer = select('#main');
  if (mainContainer) {
    setTimeout(() => {
      new ResizeObserver(function() {
        select('.echart', true).forEach(getEchart => {
          echarts.getInstanceByDom(getEchart).resize();
        })
      }).observe(mainContainer);
    }, 200);
  }

})();

//function validateForm(event) {
//	event.preventDefault();
//  
//	const inputs = document.querySelectorAll('input, textarea');
//	const submitButton = event.target;
//	const parentDiv = submitButton.closest('.text-left');
//	const errorDiv = parentDiv.nextElementSibling;
//	const errorSpan = errorDiv.querySelector('.error-message');
//	errorSpan.textContent = ""; 
//	let isValid = true;
//
//	inputs.forEach(input => {
//    
//	    const required = input.getAttribute('data-required') === 'true';
//	    const value = input.value.trim();
//	       
//	    if (required && !value) {
//	    	errorSpan.textContent = 'Please fill *required fields';
//	      	errorSpan.style.color = 'red';
//	      	isValid = false;      
//	    } else {
//			if(input.id.includes("mobile") && value){
//				const regex = /^(0\d{9}|\+94\d{9})$/;
//				if (!regex.test(value)) {
//					isValid = false;
//				  	errorSpan.textContent = 'Invalid mobile number has been entered';
//	  				errorSpan.style.color = 'red';	
//				}
//			}else if (input.id.includes("email") && value) {
//				const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;		
//			    if (!regex.test(value)){
//			    	isValid = false;
//			    	errorSpan.textContent = 'Invalid email address has been entered';
//			    	errorSpan.style.color = 'red';
//			    }
//			}
//	    }
//    
//	});
//
//  if (isValid) {
//	  console.log(event.target.closest('form'));
//    event.target.closest('form').submit();
//  }
//}

function validateForm(event) {
	event.preventDefault();
  
	const inputs = document.querySelectorAll('input, textarea, select'); // Added 'select' to the query
	const submitButton = event.target;
	const parentDiv = submitButton.closest('.text-left');
	const errorDiv = parentDiv.nextElementSibling;
	const errorSpan = errorDiv.querySelector('.error-message');
	errorSpan.textContent = ""; 
	let isValid = true;

	inputs.forEach(input => {
		const required = input.getAttribute('data-required') === 'true';
		const value = input.value.trim();
		
		if(input.getAttribute('data-valid') === 'false'){
			isValid = false;   
		}
		
		if (required && !value) {
			errorSpan.textContent = 'Please fill *required fields';
			errorSpan.style.color = 'red';
			isValid = false;      
		} else {
			if (input.tagName === 'INPUT') {
				// Mobile number validation
				if (input.id.includes("mobile") && value) {
					const regex = /^(0\d{9}|\+94\d{9})$/;
					if (!regex.test(value)) {
						isValid = false;
						errorSpan.textContent = 'Invalid mobile number has been entered';
						errorSpan.style.color = 'red';	
					}
				}
				// Email validation
				else if (input.id.includes("email") && value) {
					const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
					if (!regex.test(value)){
						isValid = false;
						errorSpan.textContent = 'Invalid email address has been entered';
						errorSpan.style.color = 'red';
					}
				}
			}
			else if (input.tagName === 'SELECT') {
			    if (required && Array.from(input.options).filter(option => option.selected).length === 0) {
			        isValid = false;
			        errorSpan.textContent = 'Please select at least one option for ' + input.id;
			        errorSpan.style.color = 'red';
			    }
			}
		}
	});

	if (isValid) {
		event.target.closest('form').submit();
	}
}


function validateUsername(input) {
  	const parentDiv = input.closest('.form-floating');
	const errorDiv = parentDiv.nextElementSibling;
	const errorSpan = errorDiv.querySelector('.error-message');
	errorSpan.textContent = ""; 
	let isValid = true;

    const username = input.value.trim();
       
    $.ajax({
		url: '/users/validateUsername',
		type: 'GET',
		traditional: true,
		data: { username: username},
		success: function (isValid) {
			if(isValid == 'true'){
				input.setAttribute("data-valid", 'true');	
			}else{
				errorSpan.textContent = 'The username is already taken';
		      	errorSpan.style.color = 'red';
		      	isValid = false; 
		      	input.setAttribute("data-valid", 'false');
			}		
		},
		error:function(status, error){
			errorSpan.textContent = 'The username is already taken';
	      	errorSpan.style.color = 'red';
	      	isValid = false; 	
	      	input.setAttribute("data-valid", 'false');
		}
	});
}

function saveImage(type, input) {
    const file = input.files[0];
    if (!file) {
        alert('No file selected.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    fetch('/uploadImage', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('Image uploaded successfully:', data.fileName);
        } else {
            alert('Error uploading image:', data.message);
        }
    })
    .catch(error => console.error('Error:', error));
}

function previewImage(event,imagePreview) {
    const reader = new FileReader();
    reader.onload = function() {
        const output = document.getElementById(imagePreview);
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
}

function previewVideo(event) {
    const videoInput = event.target;
    const file = videoInput.files[0];
    const videoPreview = document.getElementById('headerVideoPreview');
    
    if (file) {
        const fileURL = URL.createObjectURL(file);
        videoPreview.src = fileURL;
        videoPreview.style.display = 'block';
        videoPreview.load();
    }
}
