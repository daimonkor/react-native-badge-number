
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNBadgeNumberSpec.h"

@interface BadgeNumber : NSObject <NativeBadgeNumberSpec>
#else
#import <React/RCTBridgeModule.h>

@interface BadgeNumber : NSObject <RCTBridgeModule>
#endif

@end
