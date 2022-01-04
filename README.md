# Typing Teacher
A program built to improve user typing speed and accuracy. While still in the early stages, the goal of this project is to analyze the user’s typing patterns to give them words with letters they struggle with the most (keys they take the longest to find or mistype most frequently).
Typing Teacher provides an array of tests to target what the user is trying to improve on, from short burst typing to endurance modes. In addition, Typing Teacher will provide users with insights on what they need to work on and highlight their improvements to motivate them to continue practicing.
# Using Typing Teacher
![image](https://user-images.githubusercontent.com/94150901/148121685-3650fbb0-b662-4101-a5b5-d905e5f9b230.png)
Using Typing Teacher is easy. Simply launch the application and begin typing. To change the test length, click on the “x Words” text on the top right. To start a new test, press the tab key on your keyboard.
*Not implemented yet:* To view your key stats, hover over the desired key with your mouse.
# The UI
My goal while designing Typing Teacher was simplicity. As such, when it came time to design the user interface, keeping everything as clean and understandable as possible was the highest priority. The UI consists of one main screen. This is where the user types in their prompt in. Windows are drawn on top of this main screen for more options or information.
# The Code
Following suite with the goal of simplicity, I made sure to make my code as neat and simple as possible for ease of readability and less CPU load. In addition, I made the project as modular as possible. For example, the test word bank, on-screen keyboard, and UI language can all be easily changed as none are hard coded into the program.
# Development Goals
****Milestone 1**** – Alpha Release 1 – Released 1/4/2022

The first development milestone for this project is to have a working typing prompt. The program will generate a prompt from a list of words. After the user completes a test, the program will provide basic information such as WPM and accuracy after completing the test.
This milestone will have most of the UI elements implemented, with later milestones focusing on features like refinements and user data parsing.

****Milestone 2**** - In development

The second development milestone is to analyze user test data more thoroughly. Mis-keys and time between inputs will be recorded. This data will also be saved locally to the user’s computer to persist across sessions. An information tab will be added to show the user’s progress.
Additionally, the ability to hover over an on-screen key to get information about it will be implemented.

****Milestone 3****

The third milestone will mostly focus on refinements in the UI and code. The goal will be to optimize the code as much as possible and add theming options. By this time, Typing Teacher should be ready for the first release.
# Credits
Words list from https://gist.github.com/deekayen/4148741
