# LawOfTheJungle
This is a android game
## Class Architecture
## Versions
### Version v0.01
- Current Version Feature
	- Basic scheme and demo. Defined the structure of whole game.
	- Using a thread for game main loop.
	- Using surfaceview for drawing.
- Next Version Feature:
	- Clean and repair.
	- Talk about the structure.
### Version v0.02
- Current Version Feature
	- Revised the log format of previous version.
	- Fixed the bug while quiting and recovering game.
- Next Version Feature
	- Make the ball moving with cursor.
	- Build communication between game thread and surface veiw.
	- Adding Frame control.


## Recourse
- snake game: 
	http://blog.csdn.net/biaobiaoqi/article/details/6618313
 	
- Handler:
	http://infobloggall.com/2015/03/02/handlers-in-android/
	our calculation thread and handler should be in the same view class. Then Handler could get access to all UI element, bitmap etc. Handler is thread safe.
	- example for moving data from thread to UI thread.
		http://www.intertech.com/Blog/android-non-ui-to-ui-thread-communications-part-1-of-5/
- Surface view 
	- example 
		http://examples.javacodegeeks.com/android/core/ui/surfaceview/android-surfaceview-example/
	- zhihu
		http://www.zhihu.com/question/30117899
	- Game Framework example
		http://www.edu4java.com/en/androidgame/androidgame3.html
