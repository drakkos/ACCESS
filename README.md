ACCESS
======

This is a research tool developed as a collaboration between Dundee University, the University of Miami in Flordia, and IBM in New York.  It is a specialised tool, likely at this moment to be of interest only to accessibility researchers.  It has no setup or easy way to install.

INSTALLATION
============

The code is provided in three separate subsystems.  

The ACCESS engine itself is Java code and handles the implementation of the reinforcement engine and the plugins and corrections.  This is the repository you are currently viewing the readme for.

The second part of the tool is the Windows DLL file - this is a C/JNI combination that allows for the framework to bridge the gap between the sandboxed virtual machine and the underlying I/O streams.  This must be compiled and made available on your system, and the engine must be made to point to it in the Windows operating contexts.

The third part is the event listener, which is a small C program which hooks into the event streams of the underlying Windows system.  This handles the provision of socket based data regarding mouse and keyboard interaction. 


RELATED PUBLICATIONS
====================

1.  Heron, M.J., Hanson, V.L, & Ricketts, I. (In Press).  The Technical Design of the ACCESS Framework.   The Fifth ACM SIGHCI Symposium on Engineering Interactive Computing Systems. Forthcoming.  London, England.

2.  Heron, M.J., Hanson, V.L, & Ricketts, I. (2013).  Accessibility Support with the ACCESS Framework.   The International Journal of Human Computer Interaction. Forthcoming. Seattle, Washington. Doi: 10.1080/10447318.2013.768139

3.	Heron, M.J., Hanson, V.L, & Ricketts, I. (2013).  Open Source and Accessibility: advantages and limitations.   The Journal of Interaction Science. 1(1).  Cambridge, England.

4.	Heron, M.J., Hanson, V., & Ricketts, I. (2011).  Accessibility Support with the ACCESS Framework.   Digital Engagement ’11. Newcastle, United Kingdom.

5.	Heron, M.J. (2011).  The ACCESS Framework: Reinforcement Learning for Accessibility and Cognitive Support for Older Adults (Doctoral dissertation).  [Available online at http://hdl.handle.net/10588/4902]


OTHER RELEVANT PUBLICATIONS
===========================

6.  Heron, M.J (2012).  Inaccessible Through Oversight: The Need for Inclusive Game Design.   The Computer Games Journal 1(1). Glasgow, Scotland..  

7.  Vickers, S., Istance, H., Heron, M.J. (2013).  Accessible Gaming for People with Physical and Cognitive Disabilities: A Framework for Dynamic Adaptation.   Conference of Human Computer Interaction 2013. Seattle, Washington.
