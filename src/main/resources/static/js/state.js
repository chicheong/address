/*!
 * GLOBAL SCRIPT
 */

/*
 * For back to top button
 */
 // Only enable if the document has a long scroll bar
 // Note the window height + offset
 if ( ($(window).height() + 200) < $(document).height() ) {
     $('#top-link-block').removeClass('hidden').affix({
     // how far to scroll down before link "slides" into view
         offset: {top:100}
     });
 }