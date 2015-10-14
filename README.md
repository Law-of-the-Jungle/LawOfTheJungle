# LawOfTheJungle
This is a android game
## Class Architecture
## Versions
### Version v0.01
- Basic scheme and demo. Defined the structure of whole game.
- Using a thread for game main loop.
- Using surfaceview for drawing.

## Recourse
- snake game: 
	http://blog.csdn.net/biaobiaoqi/article/details/6618313
 	
- Handler:
	http://infobloggall.com/2015/03/02/handlers-in-android/
	our calculation thread and handler should be in the same view class. Then Handler could get access to all UI element, bitmap etc. Handler is thread safe.
	- example for moving data from thread to UI thread.
- Surface view 
	- example 
		http://examples.javacodegeeks.com/android/core/ui/surfaceview/android-surfaceview-example/
	- zhihu
		http://www.zhihu.com/question/30117899
	- Game Framework example
		http://www.edu4java.com/en/androidgame/androidgame3.html
