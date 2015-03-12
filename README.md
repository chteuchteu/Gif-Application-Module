# Gif-Application-Module
I've got several Android application out there  whose aim is to display gifs from different sources.
Those applications share the same code and are very similar, so it's quite difficult to maintain those without merging some code. Here is Gif Application Module, the Android module used by all of these applications.

To use it, one application should provide some information to this module in order to instantiate the magic. (the way it extracts a List<Gif> from the HTML source, for example)

It is used in '[Les Joies de l'Etudiant Info](https://play.google.com/store/apps/details?id=com.chteuchteu.lesjoiesdeletudiantinfo)' and '[Les Joies du Sysadmin](https://play.google.com/store/apps/details?id=com.chteuchteu.lesjoiesdusysadmin)'.
