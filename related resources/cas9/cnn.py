from torch import nn


class cnn(nn.Module):
    def __init__(self,depth):
        super(cnn,self).__init__()
        
        self.conv1 = nn.Sequential(
            nn.Conv2d(in_channels=depth,
                      out_channels=32,
                      kernel_size=(1,3),
                      stride=1,
                      padding=(0,1)),
                      nn.BatchNorm2d(32),
                      nn.Dropout(0.5),
                      nn.ReLU()
            )   #shape=[32,1,23]
        
        self.conv2 = nn.Sequential(
            nn.Conv2d(32,64,(1,3),2,(0,1)), nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())   #shape=[64,1,12]

        self.conv3 = nn.Sequential(
            nn.Conv2d(64,64,(1,3),1,(0,1)), nn.BatchNorm2d(64), nn.Dropout(0.5),nn.ReLU())   #shape=[64,1,12]
        
        self.conv4 = nn.Sequential(
            nn.Conv2d(64,256,(1,3),2,(0,1)), nn.BatchNorm2d(256), nn.Dropout(0.5),nn.ReLU())   #shape=[128,1,6]
        
        self.conv5 = nn.Sequential(
            nn.Conv2d(256,256,(1,3),1,(0,1)), nn.BatchNorm2d(256), nn.Dropout(0.5),nn.ReLU())   #shape=[256,1,6]
        
        #regression
        self.conv6 = nn.Sequential(
            nn.Conv2d(256,512,(1,3),2,(0,1)), nn.BatchNorm2d(512), nn.Dropout(0.5),nn.ReLU())   #shape=[512,1,3]

        self.conv7 = nn.Sequential(
            nn.Conv2d(512,1024,(1,3),1), nn.BatchNorm2d(1024),nn.Dropout(0.5), nn.ReLU())   #shape=[1024,1,1]
        self.relu = nn.ReLU()
        self.fc1 = nn.Linear(1024,256)
        self.fc2 = nn.Linear(256,1)
        self.logsoftmax = nn.LogSoftmax()

    def forward(self,x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)
        x = self.conv4(x)
        x = self.conv5(x)
        x = self.conv6(x)
        x = self.conv7(x)
        x = x.view(-1,1024) 
        x = self.relu(self.fc1(x))
        x = self.fc2(x)
        return x


class precnn(nn.Module):
    def __init__(self,depth):
        super(precnn,self).__init__()
        
        self.conv1 = nn.Sequential(
            nn.Conv2d(in_channels=depth,
                      out_channels=32,
                      kernel_size=(1,3),
                      stride=1,
                      padding=(0,1)),
                      nn.BatchNorm2d(32),
                      nn.Dropout(0.5),
                      nn.ReLU()
            )   #shape=[32,1,23]
        
        self.conv2 = nn.Sequential(
            nn.Conv2d(32,64,(1,3),2,(0,1)), nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())   #shape=[64,1,12]

        self.conv3 = nn.Sequential(
            nn.Conv2d(64,64,(1,3),1,(0,1)), nn.BatchNorm2d(64), nn.Dropout(0.5),nn.ReLU())   #shape=[64,1,12]
        
        self.conv4 = nn.Sequential(
            nn.Conv2d(64,128,(1,3),2,(0,1)), nn.BatchNorm2d(128), nn.Dropout(0.5),nn.ReLU())   #shape=[128,1,6]
        
        self.conv5 = nn.Sequential(
            nn.Conv2d(128,256,(1,3),1,(0,1)), nn.BatchNorm2d(256),nn.Dropout(0.5),nn.ReLU())
        
        self.tconv1 = nn.Sequential(
            nn.ConvTranspose2d(256,256,(1,3),1,(0,1)),nn.BatchNorm2d(256),nn.Dropout(0.5), nn.ReLU())   #shape=[256,1,6]  

        self.tconv2 = nn.Sequential(
            nn.ConvTranspose2d(256,256,(1,3),2,(0,1),output_padding = (0,1)),nn.BatchNorm2d(256),nn.Dropout(0.5), nn.ReLU())  #shape=[256,1,12] 

        self.tconv3 = nn.Sequential(
            nn.ConvTranspose2d(256,64,(1,3),2,(0,1),output_padding = 0),nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())  #shape=[64,1,23] 

        self.tconv4 = nn.Sequential(
            nn.ConvTranspose2d(64,32,(1,3),1,(0,1)),nn.BatchNorm2d(32),nn.Dropout(0.5), nn.ReLU())  #shape=[32,1,23]

        self.tconv5 = nn.Sequential(
            nn.ConvTranspose2d(32,depth,(1,3),1,(0,1)),nn.BatchNorm2d(depth),nn.Dropout(0.5), nn.ReLU())  #shape=[4,1,23]

    def forward(self,x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)
        x = self.conv4(x)
        x = self.conv5(x)
        x = self.tconv1(x)
        x = self.tconv2(x)
        x = self.tconv3(x)
        x = self.tconv4(x)
        x = self.tconv5(x)
        return x  
 
class cnn2(nn.Module):
    def __init__(self,depth):
        super(cnn2,self).__init__()
        
        self.conv1 = nn.Sequential(
            nn.Conv2d(in_channels=depth,
                      out_channels=32,
                      kernel_size=(1,3),
                      stride=1,
                      padding=(0,1)),
                      nn.BatchNorm2d(32),
                      nn.Dropout(0.5),
                      nn.ReLU()
            )   #shape=[32,1,23]
        
        self.conv2 = nn.Sequential(
            nn.Conv2d(32,64,(1,3),2,(0,1)), nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())   #shape=[64,1,12]

        self.conv3 = nn.Sequential(
            nn.Conv2d(64,64,(1,3),1,(0,1)), nn.BatchNorm2d(64), nn.Dropout(0.5),nn.ReLU())   #shape=[64,1,12]
        
        self.conv4 = nn.Sequential(
            nn.Conv2d(64,256,(1,3),2,(0,1)), nn.BatchNorm2d(256), nn.Dropout(0.5),nn.ReLU())   #shape=[256,1,6]
        
        self.conv5 = nn.Sequential(
            nn.Conv2d(256,256,(1,3),1,(0,1)), nn.BatchNorm2d(256), nn.Dropout(0.5),nn.ReLU())   #shape=[256,1,6]
        
        #regression
        self.conv6 = nn.Sequential(
            nn.Conv2d(256,512,(1,3),2,(0,1)), nn.BatchNorm2d(512), nn.Dropout(0.5),nn.ReLU())   #shape=[512,1,3]
        
        self.conv7 = nn.Sequential(
            nn.Conv2d(512,512,(1,3),1,(0,1)), nn.BatchNorm2d(512), nn.Dropout(0.5),nn.ReLU())   #shape=[512,1,3]

        self.conv8 = nn.Sequential(
            nn.Conv2d(512,1024,(1,3),1), nn.BatchNorm2d(1024),nn.Dropout(0.5), nn.ReLU())   #shape=[1024,1,1]
        
        self.relu = nn.ReLU()
        self.fc1 = nn.Linear(1024,256)
        self.fc2 = nn.Linear(256,1)
        self.logsoftmax = nn.LogSoftmax()

    def forward(self,x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)
        x = self.conv4(x)
        x = self.conv5(x)
        x = self.conv6(x)
        x = self.conv7(x)
        x = self.conv8(x)
        x = x.view(-1,1024) 
        x = self.relu(self.fc1(x))
        x = self.fc2(x)
        return x

class precnn2(nn.Module):
    def __init__(self,depth):
        super(precnn2,self).__init__()
        
        self.conv1 = nn.Sequential(
            nn.Conv2d(in_channels=depth,
                      out_channels=32,
                      kernel_size=(1,3),
                      stride=1,
                      padding=(0,1)),
                      nn.BatchNorm2d(32),
                      nn.Dropout(0.5),
                      nn.ReLU()
            )   #shape=[32,1,23]
        
        self.conv2 = nn.Sequential(
            nn.Conv2d(32,64,(1,3),2,(0,1)), nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())   #shape=[64,1,12]

        self.conv3 = nn.Sequential(
            nn.Conv2d(64,64,(1,3),1,(0,1)), nn.BatchNorm2d(64), nn.Dropout(0.5),nn.ReLU())   #shape=[64,1,12]
        
        self.conv4 = nn.Sequential(
            nn.Conv2d(64,256,(1,3),2,(0,1)), nn.BatchNorm2d(256), nn.Dropout(0.5),nn.ReLU())   #shape=[256,1,6]
        
        self.conv5 = nn.Sequential(
            nn.Conv2d(256,256,(1,3),1,(0,1)), nn.BatchNorm2d(256),nn.Dropout(0.5),nn.ReLU())
        
        self.tconv1 = nn.Sequential(
            nn.ConvTranspose2d(256,256,(1,3),1,(0,1)),nn.BatchNorm2d(256),nn.Dropout(0.5), nn.ReLU())   #shape=[256,1,6]  

        self.tconv2 = nn.Sequential(
            nn.ConvTranspose2d(256,256,(1,3),2,(0,1),output_padding = (0,1)),nn.BatchNorm2d(256),nn.Dropout(0.5), nn.ReLU())  #shape=[256,1,12] 

        self.tconv3 = nn.Sequential(
            nn.ConvTranspose2d(256,64,(1,3),2,(0,1),output_padding = 0),nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())  #shape=[64,1,23] 

        self.tconv4 = nn.Sequential(
            nn.ConvTranspose2d(64,64,(1,3),1,(0,1)),nn.BatchNorm2d(64),nn.Dropout(0.5), nn.ReLU())  #shape=[64,1,23]

        self.tconv5 = nn.Sequential(
            nn.ConvTranspose2d(64,depth,(1,3),1,(0,1)),nn.BatchNorm2d(depth),nn.Dropout(0.5), nn.ReLU())  #shape=[4,1,23]

    def forward(self,x):
        x = self.conv1(x)
        x = self.conv2(x)
        x = self.conv3(x)
        x = self.conv4(x)
        x = self.conv5(x)
        x = self.tconv1(x)
        x = self.tconv2(x)
        x = self.tconv3(x)
        x = self.tconv4(x)
        x = self.tconv5(x)
        return x  